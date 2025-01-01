<template>
  <section class="rank-list">
    <h2>Theme Rank</h2>
    <div class="rank-grid">
      <router-link v-for="rank in rankList" :key="rank.ID" :to="{ name: 'PortaDetail', params: { id: rank.ID } }"
        class="rank-card-link">
        <article class="rank-card">
          <div class="rank-image">
            <img :src="rank.IMG_PATH || '/api/placeholder/150/150'" :alt="rank.THEME_NM" />
            <div class="rank-rating">
              <i class="fas fa-star"></i>
              <span>{{ rank.RATING }}</span>
            </div>
          </div>
          <div class="rank-info">
            <h3 class="porta-title">{{ rank.THEME_NM }} <i class="bi bi-balloon-heart"></i> {{ rank.LIKES }}</h3>
            <p class="porta-content">{{ rank.PORTA_NM }}</p>
            <div class="rank-meta">
              <span class="rank-region"><i class="fas fa-map-marker-alt"></i> {{ rank.REGION_NM }}</span>
              <span class="rank-area"><i class="fas fa-map"></i> {{ rank.AREA_NM }}</span>
            </div>porta
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

    <Pagination :currentPage="currentPage" :totalPages="totalPages" @page-changed="changePage" />
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
      ranks: [],
      currentPage: 1,
      pageSize: 5
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.ranks.length / this.pageSize);
    },
    rankList() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.ranks.slice(start, end);
    },
    displayedPages() {
      const range = 2;
      let start = Math.max(1, this.currentPage - range);
      let end = Math.min(this.totalPages, this.currentPage + range);

      if (start > 1) {
        start = Math.max(1, end - (range * 2));
      }
      if (end < this.totalPages) {
        end = Math.min(this.totalPages, start + (range * 2));
      }

      return Array.from({ length: (end - start + 1) }, (_, i) => start + i);
    },
    canEditOrDelete() {
      return this.role === 'ADMIN';
    }
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
    fetchRankList() {
      axios.get('/api/porta/rankList')
        .then(response => {
          this.ranks = response.data;
        })
        .catch(error => {
          console.error('Error fetching rank list:', error);
        });
    },
    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
      }
    }
  }
};
</script>

<style scoped>
.rank-list {
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem;
  font-family: 'Arial', sans-serif;
}

h2 {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 2rem;
  text-align: center;
  text-transform: uppercase;
  letter-spacing: 2px;
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

.rank-region,
.rank-area {
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

.page-nav,
.page-number {
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

.page-number:hover,
.page-nav:hover:not(:disabled) {
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