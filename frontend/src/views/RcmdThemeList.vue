<template>
  <section class="porta-list">
    <h2 class="section-title">추천 목록</h2>
    <div class="porta-grid">
      <router-link v-for="porta in paginatedPortas" :key="porta.ID"
        :to="{ name: 'PortaDetail', params: { id: porta.ID } }" class="porta-card-link">
        <article class="porta-card">
          <div class="porta-image-container">
            <img class="porta-image" :src="porta.IMG_PATH" :alt="porta.THEME_NM" />
            <div class="porta-genre">{{ porta.GENRE_NM }}</div>
          </div>
          <div class="porta-info">
            <h3 class="porta-title">{{ porta.THEME_NM }} <i class="bi bi-balloon-heart"></i> {{ porta.LIKES }}</h3>
            <p class="porta-content">{{ porta.PORTA_NM }}</p>
            <div class="porta-meta">
              <span class="meta-item"><i class="fas fa-users"></i> {{ porta.MIN_PARTY }}-{{ porta.MAX_PARTY }}명</span>
              <span class="meta-item"><i class="fas fa-clock"></i> {{ porta.RUN_TIME }}분</span>
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
  name: 'NewThemeList',
  components: {
    Pagination,
  },
  data() {
    return {
      portas: [],
      currentPage: 1,
      pageSize: 6
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
  },
  created() {
    this.fetchNewThemeList();
  },
  methods: {
    fetchNewThemeList() {
      axios.get('api/porta/rcmdList')
        .then(response => {
          this.portas = response.data;
        })
        .catch(error => {
          console.error('Error fetching porta list:', error);
        });
    },
    changePage(page) {
      this.currentPage = page;
    },
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap');

.porta-list {
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

.porta-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
}

.porta-card-link {
  text-decoration: none;
  color: inherit;
}

.porta-card {
  background-color: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.porta-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.porta-image-container {
  position: relative;
  overflow: hidden;
  width: 100%;
  padding-top: 141.42%;
  /* 1:√2 Aspect Ratio */
}

.porta-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.porta-card:hover .porta-image {
  transform: scale(1.05);
}

.porta-genre {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 0.8rem;
}

.porta-info {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.porta-title {
  font-size: 1.4rem;
  color: #2c3e50;
  margin-bottom: 0.75rem;
  font-weight: 700;
}

.porta-content {
  font-size: 1rem;
  color: #34495e;
  margin-bottom: 1rem;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex-grow: 1;
}

.porta-meta {
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

@media (max-width: 768px) {
  .porta-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }

  .porta-title {
    font-size: 1.2rem;
  }

  .porta-content {
    font-size: 0.9rem;
  }

  .porta-meta {
    font-size: 0.8rem;
  }
}
</style>