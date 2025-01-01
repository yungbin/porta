<template>
  <div v-if="porta" class="porta-detail">
    <div class="image-container">
      <img :src="porta.IMG_PATH" :alt="porta.THEME_NM" class="porta-image" />
      <div class="image-overlay">
        <div>
          <h1>
            {{ porta.THEME_NM }}
            <button @click="toggleLike" class="btn btn-outline-danger like-button" :class="{ liked: isLiked }">
              <i class="bi bi-balloon-heart" :class="isLiked ? 'fas fa-heart' : 'far fa-heart'"></i>
            </button>
            {{ porta.LIKES }}
          </h1>
        </div>
        <p>{{ porta.PORTA_NM }}</p>
        <div class="rating">
          <span class="stars">
            <i v-for="n in 5" :key="n" class="fas fa-star" :class="{ 'filled': n <= porta.RATING }"></i>
          </span>
          {{ porta.RATING }}
        </div>
      </div>
    </div>

    <div class="content-container">
      <div class="meta-info">
        <div class="info-item">
          <i class="fas fa-map-marker-alt"></i>
          <span>{{ porta.REGION_NM }} - {{ porta.AREA_NM }}</span>
        </div>
        <div class="info-item">
          <i class="fas fa-users"></i>
          <span>{{ porta.MIN_PARTY }} - {{ porta.MAX_PARTY }} 명</span>
        </div>
        <div class="info-item">
          <i class="fas fa-clock"></i>
          <span>{{ porta.RUN_TIME }} 분</span>
        </div>
        <div class="info-item">
          <i class="fas fa-tags"></i>
          <span>{{ porta.GENRE_NM }}</span>
        </div>
      </div>

      <div class="porta-content">
        <h2>방 설명</h2>
        <p>{{ porta.CONTENT }}</p>
      </div>

      <div class="additional-info">
        <div class="info-card">
          <h3>난이도</h3>
          <div class="level-indicator">
            <div v-for="n in 5" :key="n" class="level-bar" :class="getLevelColorClass(n)"></div>
          </div>
        </div>
        <div class="info-card">
          <h3>활동성</h3>
          <div class="level-indicator">
            <div v-for="n in 5" :key="n" class="level-bar" :class="getActivityColorClass(n)"></div>
          </div>
        </div>
        <div class="info-card">
          <h3>장치비율</h3>
          <div class="level-indicator">
            <div v-for="n in 5" :key="n" class="level-bar" :class="getDeviceColorClass(n)"></div>
          </div>
        </div>
        <div class="info-card">
          <h3>가격</h3>
          <p>{{ formatPrice(porta.PRICE) }} 원</p>
        </div>
      </div>
    </div>

    <div class="review-container">
      <h2>리뷰</h2>
      <button @click="openModal" class="btn btn-primary">리뷰 작성</button>
      <Modal :visible="isModalVisible" @close="closeModal">
        <h2>리뷰 작성</h2>

        <div class="form-group">
          <label>플레이 날짜</label>
          <input type="date" v-model="playDate" class="form-control" />
        </div>

        <div class="form-group">
          <label>성공 여부</label>
          <select v-model="success" class="form-control">
            <option value="Y">성공</option>
            <option value="N">실패</option>
          </select>
        </div>

        <div class="form-group">
          <label>난이도</label>
          <div class="rating-buttons">
            <button v-for="n in 5" :key="n" @click="difficulty = n"
              :class="['rating-btn', { active: difficulty === n }]">
              {{ getDifficultyLabel(n) }}
            </button>
          </div>
        </div>

        <div class="form-group">
          <label>평점</label>
          <div class="star-rating">
            <span v-for="n in 5" :key="n" @click="rating = n" :class="['star', { filled: n <= rating }]">
              ★
            </span>
          </div>
        </div>

        <div class="form-group">
          <label>남은 시간</label>
          <TimerInput :initialMinutes="remainingMinutes" :initialSeconds="remainingSeconds"
            @change="handleTimeChange" />
        </div>

        <div class="form-group">
          <label>리뷰 내용</label>
          <textarea v-model="newReview" placeholder="리뷰를 작성하세요..." rows="4" class="form-control"></textarea>
        </div>

        <button @click="submitReview" class="btn btn-primary submit-btn">리뷰 제출</button>
      </Modal>
    </div>

    <div class="reviews-list">
      <div v-for="review in reviews" :key="review.id" class="review-card">
        <div class="review-header">
          <span class="user-name">{{ review.userName }}</span>
          <span class="review-date">{{ formatDate(review.playDate) }}</span>
        </div>
        <div class="review-body">
          <p>{{ review.content }}</p>
        </div>
        <div class="review-footer">
          <span class="review-info">성공 여부: {{ review.success === 'Y' ? '성공' : '실패' }}</span>
          <span class="review-info">난이도: {{ getDifficultyLabel(review.difficulty) }}</span>
          <span class="review-info">
            평점:
            <span class="star-rating">
              <span v-for="n in 5" :key="n" :class="['star', { filled: n <= review.rating }]">★</span>
            </span>
          </span>
          <span class="review-info">남은 시간: {{ review.remainingTime }}</span>
        </div>
      </div>
    </div>

    <div class="action-buttons">
      <router-link to="/portaList" class="btn btn-secondary">목록</router-link>
      <a :href="porta.SITE" aria-label="사이트로 이동" target="_blank" rel="noopener noreferrer" class="btn btn-primary">
        사이트로 이동
      </a>
      <div v-if="canEditOrDelete" class="edit-delete-buttons">
        <button class="btn btn-primary" @click="goToEditPage">수정</button>
        <button class="btn btn-danger" @click="deleteporta">삭제</button>
      </div>
    </div>
  </div>

  <div v-else>
    로딩 중...
  </div>
</template>

<script>
import axios from 'axios';
import { computed, ref, onMounted } from 'vue';
import { useStore } from 'vuex';
import { useRoute, useRouter } from 'vue-router';
import Modal from '@/components/Modal.vue';
import TimerInput from '@/components/TimerInput.vue';

export default {
  name: 'portaDetail',
  components: {
    Modal,
    TimerInput,
  },
  setup() {
    const store = useStore();
    const route = useRoute();
    const router = useRouter();

    const user = computed(() => store.getters.user);
    const role = computed(() => store.getters.role);

    const porta = ref(null);
    const isLiked = ref(false);
    const reviews = ref([]);
    const newReview = ref('');
    const playDate = ref(new Date().toISOString().split('T')[0]); // Set default to today's date
    const success = ref('Y');
    const difficulty = ref(1);
    const rating = ref(1);
    const isModalVisible = ref(false);
    const remainingMinutes = ref(0); // 기본값 0으로 설정
    const remainingSeconds = ref(0); // 기본값 0으로 설정

    const fetchportaDetail = async () => {
      const portaId = route.params.id;
      const userId = user.value?.id;
      try {
        const response = await axios.get(`/api/porta/detail/${portaId}`, {
          headers: {
            'User-Id': userId,
          },
          params: {
            isReaction: true,
          },
        });
        porta.value = response.data;
        isLiked.value = response.data.isLiked;
        reviews.value = response.data.reviews;
      } catch (error) {
        console.error('방 세부 정보를 가져오는 중 오류 발생:', error);
      }
    };

    const toggleLike = async () => {
      const portaId = porta.value.ID;
      const userId = user.value?.id;
      try {
        if (isLiked.value) {
          await axios.delete(`/api/porta/like/${portaId}`, {
            data: { userId }
          });
        } else {
          await axios.post(`/api/porta/like/${portaId}`, {
            userId: userId
          });
        }
        isLiked.value = !isLiked.value;
        await fetchportaDetail();
      } catch (error) {
        console.error('좋아요 상태 변경 중 오류 발생:', error);
      }
    };

    const submitReview = async () => {
      const portaId = route.params.id;
      const userId = user.value?.id;
      try {
        await axios.post(`/api/porta/review/${portaId}`, {
          userId,
          content: newReview.value,
          playDate: playDate.value,
          success: success.value,
          difficulty: difficulty.value,
          rating: rating.value,
          remainingTime: `${String(remainingMinutes.value).padStart(2, '0')}:${String(remainingSeconds.value).padStart(2, '0')}`, // mm:ss 형식으로 포맷
        });
        newReview.value = '';
        playDate.value = new Date().toISOString().split('T')[0]; // Reset to today's date
        success.value = 'Y';
        difficulty.value = 1; // 초기화
        rating.value = 1; // 초기화
        remainingMinutes.value = 0; // 초기화
        remainingSeconds.value = 0; // 초기화
        await fetchportaDetail();
        closeModal();
      } catch (error) {
        console.error('리뷰 제출 중 오류 발생:', error);
      }
    };


    const openModal = () => {
      isModalVisible.value = true;
    };

    const closeModal = () => {
      isModalVisible.value = false;
    };

    const handleTimeChange = ({ minutes, seconds }) => {
      remainingMinutes.value = minutes;
      remainingSeconds.value = seconds;
    };

    const goToEditPage = () => {
      router.push(`/portaEdit/${porta.value.ID}`);
    };

    const deleteporta = async () => {
      const portaId = porta.value.ID;
      try {
        await axios.delete(`/api/porta/portaDelete/${portaId}`);
        router.push('/portaList');
      } catch (error) {
        console.error('방 삭제 중 오류 발생:', error);
      }
    };

    const canEditOrDelete = computed(() => {
      return role.value === 'ADMIN';
    });

    const getDifficultyLabel = (n) => {
      const labels = ['매우 쉬움', '쉬움', '보통', '어려움', '매우 어려움'];
      return labels[n - 1];
    };

    const getLevelColorClass = (n) => {
      if (n <= porta.value.LEVEL) {
        if (porta.value.LEVEL <= 2) {
          return 'filled-green';
        } else if (porta.value.LEVEL === 3) {
          return 'filled-yellow';
        } else {
          return 'filled-red';
        }
      }
      return '';
    };
    const getActivityColorClass = (n) => {
      if (!porta.value) return ''; // porta이 로드되지 않았을 때
      if (n <= porta.value.ACTIVITY) {
        if (porta.value.ACTIVITY <= 2) {
          return 'filled-green';
        } else if (porta.value.ACTIVITY === 3) {
          return 'filled-yellow';
        } else {
          return 'filled-red';
        }
      }
      return '';
    };

    const getDeviceColorClass = (n) => {
      if (!porta.value) return ''; // porta이 로드되지 않았을 때
      if (n <= porta.value.DEVICE_RATIO) {
        if (porta.value.DEVICE_RATIO <= 2) {
          return 'filled-green';
        } else if (porta.value.DEVICE_RATIO === 3) {
          return 'filled-yellow';
        } else {
          return 'filled-red';
        }
      }
      return '';
    };

    const formatPrice = (price) => {
      if (price === null || price === undefined) return '';
      return price.toLocaleString(); // Format the price with commas
    };

    const formatDate = (dateString) => {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(dateString).toLocaleDateString('ko-KR', options);
    };

    onMounted(fetchportaDetail);

    return {
      porta,
      isLiked,
      reviews,
      newReview,
      playDate,
      success,
      difficulty,
      rating,
      toggleLike,
      getLevelColorClass,
      getActivityColorClass,
      getDeviceColorClass,
      formatPrice,
      isModalVisible,
      submitReview,
      openModal,
      closeModal,
      goToEditPage,
      deleteporta,
      canEditOrDelete,
      getDifficultyLabel,
      formatDate,
      remainingMinutes,
      remainingSeconds,
      handleTimeChange,
    };
  },
};
</script>

<style scoped>
.porta-detail {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
}

.porta-detail {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
}

.image-container {
  position: relative;
  width: 100%;
  height: 400px;
  overflow: hidden;
  border-radius: 10px;
  margin-bottom: 20px;
}

.porta-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
  color: white;
  padding: 20px;
}

.image-overlay h1 {
  margin: 0;
  font-size: 2.5em;
}

.like-button {
  background: transparent;
  border: none;
  color: #fff;
  font-size: 1.2em;
  margin-left: 10px;
}

.like-button.liked {
  color: #d5006d;
}

.rating {
  margin-top: 10px;
  font-size: 1.2em;
}

.stars {
  color: #ffd700;
  margin-right: 10px;
}

.stars .filled {
  color: #ffd700;
}

.content-container {
  background-color: white;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.meta-info {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.info-item i {
  margin-right: 10px;
  color: #007bff;
}

.porta-content {
  margin-bottom: 20px;
}

.porta-content p {
  white-space: pre-line;
}

.additional-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.info-card {
  background-color: #f8f9fa;
  border-radius: 5px;
  padding: 15px;
  text-align: center;
}

.level-indicator {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.level-bar {
  width: 20px;
  height: 10px;
  background-color: #e9ecef;
  margin: 0 2px;
}

.level-bar.filled-green {
  background-color: #28a745;
  /* 초록색 */
}

.level-bar.filled-yellow {
  background-color: #ffc107;
  /* 노란색 */
}

.level-bar.filled-red {
  background-color: #dc3545;
  /* 빨간색 */
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.btn {
  padding: 10px 20px;
  border-radius: 5px;
  text-decoration: none;
  font-weight: bold;
  transition: background-color 0.3s;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-danger {
  background-color: #dc3545;
  color: white;
}

.btn:hover {
  opacity: 0.8;
}

.review-container {
  margin-top: 20px;
}

.review-container textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ced4da;
  border-radius: 5px;
  margin-bottom: 10px;
  resize: vertical;
}

.reviews-list {
  margin-top: 20px;
}

.review-item {
  background-color: #f8f9fa;
  border-radius: 5px;
  padding: 10px;
  margin-bottom: 10px;
}

@media (max-width: 768px) {
  .meta-info {
    flex-direction: column;
  }

  .additional-info {
    grid-template-columns: 1fr;
  }
}

.review-container {
  margin-top: 20px;
}

.rating-input {
  margin: 10px 0;
}

input[type="date"],
select,
textarea {
  max-width: 100%;
  /* 최대 폭을 100%로 설정 */
}

.success-input {
  margin: 10px 0;
}

.time-input {
  margin: 10px 0;
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  max-width: 500px;
  width: 100%;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-control {
  width: 100%;
  /* 가로 폭을 100%로 설정 */
  padding: 8px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  box-sizing: border-box;
  /* 패딩과 테두리를 포함한 총 너비를 설정 */
}

.rating-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.rating-btn {
  padding: 5px 10px;
  border: 1px solid #007bff;
  background-color: #fff;
  color: #007bff;
  cursor: pointer;
  transition: all 0.3s;
}

.rating-btn.active {
  background-color: #007bff;
  color: #fff;
}

.star-rating {
  font-size: 24px;
  color: #ffd700;
  cursor: pointer;
}

.star {
  color: rgb(111, 114, 116);
  /* 기본 색상: 흰색 */
  transition: color 0.3s;
  /* 색상 전환 효과 */
}

.star.filled {
  color: #ffd700;
}

.submit-btn {
  width: 100%;
  margin-top: 20px;
}

.reviews-list {
  margin-top: 30px;
}

.review-card {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.review-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.user-name {
  font-weight: bold;
}

.review-date {
  color: #6c757d;
}

.review-body {
  margin-bottom: 10px;
}

.review-footer {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.review-info {
  font-size: 0.9em;
  color: #495057;
}

@media (max-width: 768px) {
  .rating-buttons {
    flex-direction: column;
    align-items: stretch;
  }

  .rating-btn {
    margin-bottom: 5px;
  }

  .review-footer {
    flex-direction: column;
  }
}

.timer-input {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5em;
}

.time-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.time-input {
  width: 2.5em;
  text-align: center;
  font-size: 1em;
  border: none;
  background: transparent;
  color: #333;
}

.time-btn {
  background: none;
  border: none;
  font-size: 0.8em;
  cursor: pointer;
  color: #007bff;
}

.time-separator {
  margin: 0 0.5em;
}
</style>
