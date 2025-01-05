<template>
  <section class="rank-list">
    <h2>Theme Rank</h2>

    <!-- 정렬 기준 드롭다운 -->
    <div class="sort-dropdown">
      <label for="sortOptions" class="sort-label">정렬 기준:</label>
      <select id="sortOptions" v-model="sortOption" @change="sortRanks">
        <option value="rating">평점순</option>
        <option value="likes">좋아요순</option>
        <option value="comments">리뷰순</option>
      </select>
    </div>

    <div class="rank-grid">
      <router-link 
        v-for="rank in paginatedRanks" 
        :key="rank.ID" 
        :to="{ name: 'RoomDetail', params: { id: rank.ID } }"
        class="rank-card-link"
      >
        <article class="rank-card">
          <div class="rank-image">
            <img :src="rank.IMG_PATH || '/api/placeholder/150/150'" :alt="rank.THEME_NM" />
            <div class="rank-rating">
              <i class="fas fa-star"></i>
              <span>{{ rank.RATING }}</span>
            </div>
          </div>
          <div class="rank-info">
            <h3 class="room-title">{{ rank.THEME_NM }} <i class="bi bi-balloon-heart"></i> {{ rank.LIKES }}</h3>
            <p class="room-content">{{ rank.ROOM_NM }}</p>
            <p class="room-content">review : {{ rank.COMMENTS }}</p>
            <div class="rank-meta">
              <span class="rank-region"><i class="fas fa-map-marker-alt"></i> {{ rank.REGION_NM }}</span>
              <span class="rank-area"><i class="fas fa-map"></i> {{ rank.AREA_NM }}</span>
            </div>
          </div>
          <div class="rank-details">
            <div class="info-item">
              <i class="fas fa-users"></i>
              <span>{{ rank.MIN_PARTY }} - {{ rank.MAX_PARTY }} 명</span>
            </div>
            <div class="info-item">
              <i class="fas fa-clock"></i>
              <span>{{ rank.RUN_TIME }} 분</span>
            </div>
            <div class="info-item">
              <i class="fas fa-won-sign"></i>
              <span>{{ rank.PRICE }} 원</span>
            </div>
          </div>
        </article>
      </router-link>
    </div>

    <Pagination
      :currentPage="currentPage"
      :totalPages="totalPages"
      @page-changed="changePage"
    />
  </section>
</template>

<script>
import axios from 'axios';
import { computed } from 'vue';
import { useStore } from 'vuex';
import Pagination from '@/components/Pagination.vue';

export default {
  name: 'RankList',
  components: {
    Pagination,
  },
  data() {
    return {
      ranks: [], // 모든 rank 목록
      currentPage: 1, // 현재 페이지
      pageSize: 5, // 페이지 당 아이템 수 (5개씩 표시)
      sortOption: 'rating', // 정렬 기준 (기본: 평점순)
    };
  },
  computed: {
    // 전체 페이지 수 계산 (하지만 4페이지만 보이게 설정)
    totalPages() {
      return Math.min(Math.ceil(this.ranks.length / this.pageSize), 4); // 최대 4페이지까지만 표시
    },
    // 정렬된 목록을 반환
    sortedRanks() {
      const sortedRanks = [...this.ranks];

      switch (this.sortOption) {
        case 'rating':
          return sortedRanks.sort((a, b) => b.RATING - a.RATING); // 평점순 (내림차순)
        case 'likes':
          return sortedRanks.sort((a, b) => b.LIKES - a.LIKES); // 좋아요순 (내림차순)
        case 'comments':
          return sortedRanks.sort((a, b) => b.COMMENTS - a.COMMENTS); // 댓글순 (내림차순)
        default:
          return sortedRanks;
      }
    },
    // 현재 페이지에 맞는 데이터만 반환 (5개씩 보여줌)
    paginatedRanks() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.sortedRanks.slice(start, end); // 현재 페이지에 맞는 데이터만 반환
    },
    // 최대 4개 페이지 번호만 표시 (페이징 UI)
    displayedPages() {
      const range = 2; // 현재 페이지 기준으로 왼쪽, 오른쪽 각각 2개의 페이지 번호만 보이게
      let start = Math.max(1, this.currentPage - range);
      let end = Math.min(this.totalPages, this.currentPage + range);

      // 페이지 범위가 4개가 되도록 조정
      if (end - start < 3) {
        end = Math.min(this.totalPages, start + 3);
      }
      if (start > 1) {
        start = Math.max(1, end - 3);
      }

      return Array.from({ length: end - start + 1 }, (_, i) => start + i);
    },
  },
  created() {
    this.fetchRankList();
  },
  setup() {
    const store = useStore();
    const user = computed(() => store.getters.user);
    const role = computed(() => store.getters.role);

    return {
      user,
      role,
    };
  },
  methods: {
    // API에서 rank 목록을 가져옴 (전체 데이터를 가져옴)
    fetchRankList() {
      axios.get('/api/room/rankList')
        .then(response => {
          this.ranks = response.data; // 전체 데이터 저장
        })
        .catch(error => {
          console.error('Error fetching rank list:', error);
        });
    },
    // 페이지 변경
    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
      }
    },
    // 정렬 기준 변경
    sortRanks() {
      this.currentPage = 1; // 정렬 기준 변경 시 첫 페이지로 돌아가기
    },
  },
};
</script>

<style scoped>
.rank-list {
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem;
  font-family: 'Arial', sans-serif;
  position: relative;
}

h2 {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 2rem;
  text-align: center;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.sort-dropdown {
  position: absolute;
  top: 1rem;
  right: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.sort-dropdown select {
  padding: 0.5rem;
  font-size: 1rem;
  border-radius: 4px;
  border: 1px solid #ccc;
  background-color: #fff;
  cursor: pointer;
}

.sort-label {
  font-size: 1rem;
  color: #2c3e50;
}

.rank-grid {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.rank-card-link {
  text-decoration: none;
  color: inherit;
}

.rank-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.rank-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.rank-image {
  position: relative;
  width: 150px;
  height: 150px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.rank-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.rank-rating {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: rgba(0, 0, 0, 0.7);
  color: #fff;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 5px;
}

.rank-info {
  flex: 1;
}

.rank-title {
  font-size: 1.5rem;
  color: #3498db;
  margin-bottom: 0.5rem;
}

.rank-content {
  font-size: 1.1rem;
  color: #34495e;
  margin-bottom: 0.5rem;
}

.rank-meta {
  display: flex;
  gap: 1rem;
  font-size: 0.9rem;
  color: #7f8c8d;
  margin-bottom: 0.5rem;
}

.rank-region, .rank-area {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.rank-details {
  display: flex;
  gap: 1rem;
  font-size: 0.9rem;
  color: #34495e;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.info-item i {
  color: #3498db;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
  margin-top: 2rem;
}

.page-nav, .page-number {
  background-color: #fff;
  color: #3498db;
  border: 1px solid #3498db;
  border-radius: 4px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.page-nav {
  padding: 0.5rem 0.8rem;
}

.page-nav:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-number:hover, .page-nav:hover:not(:disabled) {
  background-color: #3498db;
  color: #fff;
}

.page-number.active {
  background-color: #3498db;
  color: #fff;
  font-weight: bold;
}

@media (max-width: 768px) {
  .rank-card {
    flex-direction: column;
    align-items: flex-start;
  }

  .rank-image {
    width: 100%;
    height: 200px;
  }

  .rank-details {
    flex-direction: column;
    gap: 0.5rem;
  }
}
</style>
