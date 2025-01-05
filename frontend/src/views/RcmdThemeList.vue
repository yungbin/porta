<template>
  <section class="room-list">
    <!-- 드롭다운 메뉴 -->
    <div class="dropdown">
      <button class="dropbtn">{{ selectedOption }}</button>
      <div class="dropdown-content">
        <a href="#" @click="changeSelection('추천 목록', 'api/room/rcmdList')">추천 목록</a>
        <a href="#" @click="changeSelection('신규', 'api/room/newList')">신규</a>
        <a href="#" @click="changeSelection('신규', 'api/room/newList')">생성ai사이트</a>
        <a href="#" @click="changeSelection('신규', 'api/room/newList')">news</a>
        <a href="#" @click="changeSelection('신규', 'api/room/newList')">사이드프로젝트</a>
      </div>
    </div>

    <h2 class="section-title">{{ selectedOption }}</h2>

    <div class="room-grid">
      <router-link v-for="room in paginatedRooms" :key="room.ID" :to="{ name: 'RoomDetail', params: { id: room.ID } }"
        class="room-card-link">
        <article class="room-card">
          <div class="room-image-container">
            <img class="room-image" :src="room.IMG_PATH" :alt="room.THEME_NM" />
            <div class="room-genre">{{ room.GENRE_NM }}</div>
          </div>
          <div class="room-info">
            <h3 class="room-title">{{ room.THEME_NM }} <i class="bi bi-balloon-heart"></i> {{ room.LIKES }}</h3>
            <p class="room-content">{{ room.ROOM_NM }}</p>
            <div class="room-meta">
              <span class="meta-item"><i class="fas fa-users"></i> {{ room.MIN_PARTY }}-{{ room.MAX_PARTY }}명</span>
              <span class="meta-item"><i class="fas fa-clock"></i> {{ room.RUN_TIME }}분</span>
              <span class="meta-item"><i class="fas fa-star"></i> 평점 {{ room.RATING }}</span>
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

export default {
  name: 'RoomList',
  components: {
    Pagination,
  },
  data() {
    return {
      rooms: [],
      currentPage: 1,
      pageSize: 6,
      selectedOption: '추천 목록',  // 초기 선택값을 '전체'로 설정
      apiEndpoint: 'api/room/rcmdList',  // 기본 API 경로
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.rooms.length / this.pageSize);
    },
    paginatedRooms() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.rooms.slice(start, end);
    },
  },
  created() {
    this.fetchRoomList();
  },
  methods: {
    fetchRoomList() {
      axios.get(this.apiEndpoint)
        .then(response => {
          this.rooms = response.data;
        })
        .catch(error => {
          console.error('Error fetching room list:', error);
        });
    },
    changePage(page) {
      this.currentPage = page;
    },
    // 드롭다운 선택에 따른 API 경로 및 제목 변경
    changeSelection(option, endpoint) {
      this.selectedOption = option;
      this.apiEndpoint = endpoint;
      this.fetchRoomList();  // 선택된 항목에 맞는 데이터 새로고침
    }
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap');

.room-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 3rem 1rem;
  font-family: 'Noto Sans KR', sans-serif;
}

.section-title {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 2rem;
  text-align: center;
  font-weight: 700;
}

.room-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
}

.room-card-link {
  text-decoration: none;
  color: inherit;
}

.room-card {
  background-color: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.room-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.room-image-container {
  position: relative;
  overflow: hidden;
  width: 100%;
  padding-top: 141.42%;
  /* 1:√2 Aspect Ratio */
}

.room-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.room-card:hover .room-image {
  transform: scale(1.05);
}

.room-genre {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 0.8rem;
}

.room-info {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.room-title {
  font-size: 1.4rem;
  color: #2c3e50;
  margin-bottom: 0.75rem;
  font-weight: 700;
}

.room-content {
  font-size: 1rem;
  color: #34495e;
  margin-bottom: 1rem;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex-grow: 1;
}

.room-meta {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
  color: #7f8c8d;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.3rem;
}

.meta-item i {
  font-size: 1rem;
}

.dropdown {
  position: relative;
  display: inline-block;
  font-size: 1.2rem;
  float: right;
  /* 버튼을 오른쪽으로 배치 */
}

.dropbtn {
  background-color: #3498db;
  color: white;
  padding: 10px 20px;
  font-size: 1rem;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

.dropdown-content {
  display: none;
  position: absolute;
  right: 0;
  /* 드롭다운 메뉴를 버튼의 오른쪽에 표시 */
  top: 100%;
  /* 버튼 바로 아래에 표시 */
  background-color: #f9f9f9;
  min-width: 160px;
  box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
  z-index: 1;
  border-radius: 5px;
}

.dropdown:hover .dropdown-content {
  display: block;
}

.dropdown-content a {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

.dropdown-content a:hover {
  background-color: #ddd;
}
</style>
