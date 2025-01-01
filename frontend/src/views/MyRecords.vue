<template>
  <section class="porta-list">
    <h2>My Recodes</h2>
    <div class="search-container">
      <div class="search-input-wrapper">
        <input type="text" v-model="searchKeyword" placeholder="검색어를 입력하세요" class="search-input"
          @keyup.enter="fetchPortaaList" />
      </div>
      <div class="select-wrapper">
        <select v-model="selectedRegion" class="search-select">
          <option value="">지역을 선택하세요</option>
          <option v-for="region in regions" :key="region.CODE_VAL" :value="region.CODE_VAL">
            {{ region.CODE_NM }}
          </option>
        </select>
      </div>
      <div class="select-wrapper">
        <select v-model="selectedGenre" class="search-select">
          <option value="">장르를 선택하세요</option>
          <option v-for="genre in genres" :key="genre.CODE_VAL" :value="genre.CODE_VAL">
            {{ genre.CODE_NM }}
          </option>
        </select>
      </div>
      <button @click="fetchPortaList" class="btn btn-primary search-btn">
        검색
      </button>
    </div>

    <div class="porta-grid">
      <router-link v-for="porta in paginatedPortas" :key="porta.ID"
        :to="{ name: 'PortaDetail', params: { id: porta.ID } }" class="porta-card-link">
        <article class="porta-card">
          <div class="porta-image-container">
            <img v-if="porta.IMG_PATH" :src="porta.IMG_PATH" :alt="porta.THEME_NM" class="porta-image"
              @error="onImageError(porta.ID)" />
            <img v-else src="/api/placeholder/150/150" :alt="porta.THEME_NM" class="porta-image placeholder" />
          </div>
          <div class="porta-info">
            <h3 class="porta-title">{{ porta.THEME_NM }}</h3>
            <p class="porta-content">{{ porta.PORTA_NM }}</p>
            <div class="porta-meta">
              <span class="porta-genre">장르: {{ porta.GENRE_NM }}</span>
              <span class="porta-party">추천 인원: {{ porta.MIN_PARTY }}-{{ porta.MAX_PARTY }}명</span>
              <span class="porta-time">시간: {{ porta.RUN_TIME }}분</span>
              <span class="porta-price">{{ porta.PRICE }}원</span>
            </div>
          </div>
        </article>
      </router-link>
    </div>

    <Pagination :currentPage="currentPage" :totalPages="totalPages" @page-changed="changePage" />
  </section>
</template>

<script>
import axios from 'axios';
import Pagination from '@/components/Pagination.vue';
import { computed } from 'vue';
import { useStore } from 'vuex';

export default {
  name: 'PortaList',
  components: {
    Pagination,
  },
  data() {
    return {
      portas: [],
      currentPage: 1,
      pageSize: 6,
      searchKeyword: '',
      selectedRegion: '',
      selectedGenre: '',
      regions: [], // 지역 데이터를 여기로 받아옴
      genres: [],  // 장르 데이터를 여기로 받아옴
    };
  },
  setup() {
    const store = useStore();

    const isLoggedIn = computed(() => store.getters.isAuthenticated);
    const user = computed(() => store.getters.user);
    const role = computed(() => store.getters.role);

    return {
      isLoggedIn,
      user,
      role,
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.portas.length / this.pageSize);
    },
    paginatedPortas() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.portas.slice(start, end);
    },
    roleAdmin() {
      return this.role === 'ADMIN';
    }
  },
  created() {
    this.fetchPortaList();  // 처음 페이지가 로드될 때 방 목록을 조회
    this.fetchRegions();    // 지역 목록 불러오기
    this.fetchGenres();     // 장르 목록 불러오기
  },
  methods: {
    fetchPortaList() {
      const params = {
        userId: this.user?.id,
        keyword: this.searchKeyword.replace(/\s+/g, '') || '',  // 검색어가 없을 때는 빈 값
        region: this.selectedRegion || '',  // 선택된 지역이 없을 때는 빈 값
        genre: this.selectedGenre || '',    // 선택된 장르가 없을 때는 빈 값
      };

      axios
        .get('api/porta/myRecords', { params })
        .then((response) => {
          this.portas = response.data;
          this.currentPage = 1; // Reset to first page on new search
        })
        .catch((error) => {
          console.error('Error fetching porta list:', error);
        });
    },
    fetchRegions() {
      const type = "REGION_CD";
      axios
        .get(`/api/common/code/${type}`)
        .then((response) => {
          this.regions = response.data;
        })
        .catch((error) => {
          console.error('Error fetching regions:', error);
        });
    },
    fetchGenres() {
      const type = "GENRE_CD";
      axios
        .get(`/api/common/code/${type}`)
        .then((response) => {
          this.genres = response.data;
        })
        .catch((error) => {
          console.error('Error fetching genres:', error);
        });
    },
    changePage(page) {
      this.currentPage = page;
    },
    onImageError(id) {
      console.error(`Failed to load image for porta ${id}`);
    },
  },
};
</script>


<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap');

.add-porta-button {
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;
}

.add-porta-button .btn-primary {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  font-weight: bold;
  text-align: center;
  text-decoration: none;
}

.add-porta-button .btn-primary:hover {
  background-color: #0056b3;
}

.porta-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

h2 {
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 2rem;
  text-align: center;
}

.porta-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
}

.porta-card-link {
  text-decoration: none;
  color: inherit;
}

.porta-card {
  display: flex;
  background-color: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.porta-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.porta-image-container {
  flex-shrink: 0;
  width: 100px;
  /* 고정 너비 */
  height: 150px;
  /* 고정 높이 */
  overflow: hidden;
  /* 잘리지 않게 숨김 처리 */
  border-radius: 12px;
  /* 컨테이너에 둥글게 하기 */
}

.porta-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  /* 비율을 유지하며 잘림 */
  border-radius: 12px;
  /* 이미지에 둥글게 하기 */
}

.porta-image.placeholder {
  object-fit: contain;
  /* 기본 이미지 비율 유지 */
  padding: 1rem;
  background-color: #f0f0f0;
}

.porta-info {
  flex-grow: 1;
  padding: 1rem;
  display: flex;
  flex-direction: column;
}

.porta-title {
  font-size: 1.2rem;
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.porta-content {
  font-size: 1rem;
  color: #34495e;
  margin-bottom: 0.5rem;
  line-height: 1.4;
  flex-grow: 1;
}

.porta-meta {
  font-size: 0.9rem;
  color: #7f8c8d;
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.porta-meta span {
  background-color: #ecf0f1;
  padding: 0.2rem 0.5rem;
  border-radius: 4px;
}

@media (max-width: 600px) {
  .porta-card {
    flex-direction: column;
    /* 모바일에서 세로로 쌓임 */
  }

  .porta-image-container {
    width: 100%;
    /* 너비를 100%로 설정 */
    height: 200px;
    /* 높이 조정 */
  }

  .porta-image {
    width: 100%;
    height: 100%;
    object-fit: contain;
    /* 여기 수정 */
  }
}

.search-container {
  display: flex;
  flex-wrap: wrap;
  /* 요소들이 줄 바꿈 할 수 있도록 설정 */
  gap: 2px;
  /* 요소 간의 간격을 줄여서 겹침 방지 */
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.search-input-wrapper,
.select-wrapper {
  flex: 1;
  /* 각 요소가 가능한 공간을 균등하게 차지 */
  min-width: 150px;
  /* 최소 너비를 설정하여 너무 좁아지지 않도록 함 */
  position: relative;
}

.search-input {
  width: 84%;
  /* 너비를 100%로 설정 */
  padding: 12px;
  /* 상하 패딩 */
  border: 1px solid #ced4da;
  border-radius: 5px;
  font-size: 16px;
  transition: border-color 0.3s;

}

.search-select {
  width: 95%;
  /* 너비를 100%로 설정 */
  padding: 12px;
  /* 상하 패딩 */
  border: 1px solid #ced4da;
  border-radius: 5px;
  font-size: 16px;
  transition: border-color 0.3s;
  -webkit-appearance: none;
  /* Safari 및 Chrome의 기본 스타일 제거 */
  -moz-appearance: none;
  /* Firefox의 기본 스타일 제거 */
  appearance: none;
  /* 모든 브라우저의 기본 스타일 제거 */
  background-color: white;
  /* 배경 색 설정 (선택 사항) */
}

.search-input:focus,
.search-select:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}

.select-wrapper::after {
  content: '';
  /* 화살표 아이콘 대체 */
  position: absolute;
  right: 30px;
  /* 오른쪽에서 간격 설정 */
  top: 50%;
  /* 세로 중앙 정렬 */
  transform: translateY(-50%);
  /* 세로 중앙 정렬 */
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  /* 화살표 모양을 만들기 위해 */
  border-right: 5px solid transparent;
  /* 화살표 모양을 만들기 위해 */
  border-top: 5px solid #000;
  /* 화살표 색상 설정 */
  pointer-events: none;
  /* 클릭 방지 */
}

.search-icon {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #6c757d;
  pointer-events: none;
  /* 아이콘이 입력을 방해하지 않도록 설정 */
}

.search-btn {
  padding: 12px 25px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-btn:hover {
  background-color: #0056b3;
}

/* 모바일 디바이스에 대한 스타일 */
@media (max-width: 768px) {
  .search-container {
    flex-direction: column;
    /* 요소들을 세로로 쌓이도록 설정 */
    align-items: stretch;
    /* 각 요소가 부모 너비를 다 차지하도록 설정 */
  }
}
</style>