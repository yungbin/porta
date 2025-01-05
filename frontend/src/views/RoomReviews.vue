<template>
  <div v-if="room" class="room-detail">
    <div class="image-container">
      <img :src="room.IMG_PATH" :alt="room.THEME_NM" class="room-image" />
      <div class="image-overlay">
        <div>
          <h1>
            {{ room.THEME_NM }}
          </h1>
        </div>
        <p>{{ room.ROOM_NM }}</p>
        <div class="rating">
          <span class="stars">
            <i v-for="n in 5" :key="n" class="fas fa-star" :class="{ 'filled': n <= room.RATING }"></i>
          </span>
          평점 : {{ room.RATING }}
        </div>
      </div>
    </div>

    <div class="review-container">
      <div class="review-header-section">
        <h2>리뷰</h2>
        <button v-if="showReviewButton" @click="openModal" class="btn btn-primary review-write-btn">
          <i class="fas fa-pen"></i> 리뷰 작성
        </button>
      </div>
    
    <Modal :visible="isModalVisible" @close="closeModal">
      <h2>리뷰 작성</h2>

      <div class="form-group">
        <label>플레이 날짜</label>
        <input type="date" v-model="playDate" class="form-control" />
      </div>

      <div class="form-group">
        <label>성공 여부</label>
        <select v-model="successFlag" class="form-control">
          <option value="Y">성공</option>
          <option value="N">실패</option>
        </select>
      </div>

      <div class="form-group">
        <label>난이도</label>
        <div class="rating-buttons">
          <button 
            v-for="n in 5" 
            :key="n" 
            @click="difficulty = n" 
            :class="['rating-btn', { active: difficulty === n }]"
          >
            {{ getDifficultyLabel(n) }}
          </button>
        </div>
      </div>

      <div class="form-group">
        <label>평점</label>
        <div class="star-rating">
          <span 
            v-for="n in 5" 
            :key="n" 
            @click="rating = n" 
            :class="['star', { filled: n <= rating }]"
          >
            ★
          </span>
        </div>
      </div>

      <div class="form-group">
          <label>남은 시간</label>
          <TimerInput 
            :initialMinutes="leftMinutes"
            :initialSeconds="leftSeconds"
            @change="handleTimeChange"
          />
      </div>

      <div class="form-group">
        <label>리뷰 내용</label>
        <textarea v-model="newReview" placeholder="리뷰를 작성하세요..." rows="4" class="form-control"></textarea>
      </div>

      <button @click="submitReview" class="btn btn-primary submit-btn">리뷰 제출</button>
    </Modal>

      <div class="reviews-list">
        <div v-for="review in reviews" :key="review.id" class="review-card">
          <div class="review-header">
            <div class="user-info">
              <i class="fas fa-user-circle"></i>
              <span class="user-name">{{ review.USER_ID }}</span>
            </div>
            <span class="review-date">{{ formatDate(review.PLAY_DATE) }}</span>
          </div>
          
          <div class="review-body">
            <p>{{ review.CONTENT }}</p>
          </div>
          
          <div class="review-footer">
            <div class="review-info success-status">
              <i class="fas fa-flag-checkered"></i>
              <span>{{ review.SUCCESS_FLAG === 'Y' ? '성공' : '실패' }}</span>
            </div>
            
            <div class="review-info difficulty">
              <i class="fas fa-chart-bar"></i>
              <span>난이도: {{ getDifficultyLabel(review.LEVEL) }}</span>
            </div>
            
            <div class="review-info rating">
              <i class="fas fa-star"></i>
              <div class="star-rating">
                <span 
                  v-for="n in 5" 
                  :key="n" 
                  class="star"
                  :class="{ 'filled': n <= review.RATING }"
                >★</span>
              </div>
            </div>
            
            <div class="review-info time">
              <i class="fas fa-clock"></i>
              <span>남은 시간: {{ review.LEFT_TIME }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="action-buttons">
      <router-link to="/roomList" class="btn btn-secondary">목록</router-link>
      <a :href="room.SITE" aria-label="사이트로 이동" target="_blank" rel="noopener noreferrer" class="btn btn-primary">
        사이트로 이동
      </a>
      <div v-if="canEditOrDelete" class="edit-delete-buttons">
        <button class="btn btn-primary" @click="goToEditPage">수정</button>
        <button class="btn btn-danger" @click="deleteRoom">삭제</button>
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
  name: 'RoomDetail',
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

    const room = ref(null);
    const isLiked = ref(false);
    const reviews = ref([]);
    const showAllReviews = ref(false);
    const newReview = ref('');
    const playDate = ref(new Date().toISOString().split('T')[0]); // Set default to today's date
    const successFlag = ref('Y');
    const difficulty = ref(1);
    const rating = ref(1);
    const isModalVisible = ref(false);
    const leftMinutes = ref(0); // 기본값 0으로 설정
    const leftSeconds = ref(0); // 기본값 0으로 설정
    
    const displayedReviews = computed(() => {
      return showAllReviews.value ? reviews.value : reviews.value.slice(0, 3);
    });

    const fetchRoomDetail = async () => {
      const roomId = route.params.id;
      const userId = user.value?.id;
      try {
        const response = await axios.get(`/api/room/detail/${roomId}`, {
          headers: {
            'User-Id': userId,
          },
          params: {
            isReaction: true,
          },
        });
        room.value = response.data;
        isLiked.value = response.data.isLiked;
        reviews.value = response.data.reviews;
        showAllReviews.value = false; // 초기 로드시 3개만 보이도록 리셋
      } catch (error) {
        console.error('방 세부 정보를 가져오는 중 오류 발생:', error);
      }
    };

    const showAllReviewsPage = () => {
      router.push(`/room/${route.params.id}/reviews`);
    };

    const toggleLike = async () => {
      const roomId = room.value.ID;
      const userId = user.value?.id;
      try {
        if (isLiked.value) {
          await axios.delete(`/api/room/like/${roomId}`, {
            data: { userId }
          });
        } else {
          await axios.post(`/api/room/like/${roomId}`, {
            userId: userId
          });
        }
        isLiked.value = !isLiked.value;
        await fetchRoomDetail();
      } catch (error) {
        console.error('좋아요 상태 변경 중 오류 발생:', error);
      }
    };

    const submitReview = async () => {
      const roomId = route.params.id;
      const userId = user.value?.id;
      const data = {roomId : roomId,
                    userId : userId,
                    content: newReview.value,
                    playDate: playDate.value,
                    successFlag: successFlag.value,
                    difficulty: difficulty.value,
                    rating: rating.value,
                    leftTime: `${String(leftMinutes.value).padStart(2, '0')}:${String(leftSeconds.value).padStart(2, '0')}`, // mm:ss 형식으로 포맷
      };
      try {
        await axios.post(`/api/room/review`, data);
        newReview.value = '';
        playDate.value = new Date().toISOString().split('T')[0]; // Reset to today's date
        successFlag.value = 'Y';
        difficulty.value = 1; // 초기화
        rating.value = 1; // 초기화
        leftMinutes.value = 0; // 초기화
        leftSeconds.value = 0; // 초기화
        await fetchRoomDetail();
        closeModal();
      } catch (error) {
        console.error('리뷰 제출 중 오류 발생:', error);
      }
    };


    const openModal = () => {
      if (!user.value) {
        alert('로그인 이후에 이용 가능합니다.');
        return;
      }
      isModalVisible.value = true;
    };

    const closeModal = () => {
      isModalVisible.value = false;
    };

    const handleTimeChange = ({ minutes, seconds }) => {
      leftMinutes.value = minutes;
      leftSeconds.value = seconds;
    };
    
    const goToEditPage = () => {
      router.push(`/roomEdit/${room.value.ID}`);
    };

    const deleteRoom = async () => {
      const roomId = room.value.ID;
      try {
        await axios.delete(`/api/room/roomDelete/${roomId}`);
        router.push('/roomList');
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
      if (n <= room.value.LEVEL) {
        if (room.value.LEVEL <= 2) {
          return 'filled-green';
        } else if (room.value.LEVEL === 3) {
          return 'filled-yellow';
        } else {
          return 'filled-red';
        }
      }
      return '';
    };
    const getActivityColorClass = (n) => {
      if (!room.value) return ''; // room이 로드되지 않았을 때
      if (n <= room.value.ACTIVITY) {
        if (room.value.ACTIVITY <= 2) {
          return 'filled-green';
        } else if (room.value.ACTIVITY === 3) {
          return 'filled-yellow';
        } else {
          return 'filled-red';
        }
      }
      return '';
    };

    const getDeviceColorClass = (n) => {
      if (!room.value) return ''; // room이 로드되지 않았을 때
      if (n <= room.value.DEVICE_RATIO) {
        if (room.value.DEVICE_RATIO <= 2) {
          return 'filled-green';
        } else if (room.value.DEVICE_RATIO === 3) {
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
    const hasUserReview = computed(() => {
      return reviews.value.some(review => review.USER_ID === user.value?.id);
    });

    const showReviewButton = computed(() => {
      return user.value && !hasUserReview.value;
    });
    onMounted(fetchRoomDetail);

    return {
      showAllReviews,
      displayedReviews,
      showAllReviewsPage,
      hasUserReview,
      showReviewButton,
      room,
      isLiked,
      reviews,
      newReview,
      playDate,
      successFlag,
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
      deleteRoom,
      canEditOrDelete,
      getDifficultyLabel,
      formatDate,
      leftMinutes,
      leftSeconds,
      handleTimeChange,
    };
  },
};
</script>

<style scoped>
.room-detail {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
}

.room-detail {
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

.room-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.8), transparent);
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
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
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

.room-content {
  margin-bottom: 20px;
}

.room-content p {
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
  background-color: #28a745; /* 초록색 */
}

.level-bar.filled-yellow {
  background-color: #ffc107; /* 노란색 */
}

.level-bar.filled-red {
  background-color: #dc3545; /* 빨간색 */
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
  margin-top: 40px;
  background-color: #f8f9fa;
  border-radius: 15px;
  padding: 25px;
}

.review-header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.review-header-section h2 {
  font-size: 1.8em;
  color: #2c3e50;
  margin: 0;
}

.review-write-btn {
  background-color: #4CAF50;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.review-write-btn:hover {
  background-color: #45a049;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.review-write-btn i {
  font-size: 0.9em;
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
  display: grid;
  gap: 20px;
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
  max-width: 100%; /* 최대 폭을 100%로 설정 */
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
  width: 100%; /* 가로 폭을 100%로 설정 */
  padding: 8px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  box-sizing: border-box; /* 패딩과 테두리를 포함한 총 너비를 설정 */
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
  display: inline-flex;
  gap: 2px;
}

.star {
  color: #e0e0e0;
  font-size: 1.1em;
}

.star.filled {
  color: #f1c40f;
}

.success-status {
  color: #2ecc71;
}

.success-status i {
  color: #2ecc71;
}

.submit-btn {
  width: 100%;
  margin-top: 20px;
}

.reviews-list {
  margin-top: 30px;
}

.review-card {
  background-color: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  transition: all 0.3s ease;
}

.review-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0,0,0,0.1);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 12px;
  border-bottom: 1px solid #eee;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-info i {
  font-size: 1.2em;
  color: #3498db;
}

.user-name {
  font-size: 1.1em;
  font-weight: 600;
  color: #2c3e50;
}

.review-date {
  font-size: 0.9em;
  color: #7f8c8d;
}

.review-body {
  padding: 5px 0;
  color: #34495e;
  line-height: 1.6;
}

.review-footer {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 15px;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.review-info {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #2c3e50;
}
@media (max-width: 768px) {
  .review-header-section {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .review-footer {
    grid-template-columns: 1fr 1fr;
  }
  
  .star-rating {
    font-size: 0.9em;
  }
}

@media (max-width: 480px) {
  .review-footer {
    grid-template-columns: 1fr;
  }
}
@media (max-width: 768px) {
  .rating-buttons {
    flex-direction: column;
    align-items: stretch;
  }

  .rating-btn {
    margin-bottom: 5px;
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

.review-more-section {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

.view-more-btn {
  padding: 10px 20px;
  background-color: transparent;
  border: 2px solid #007bff;
  color: #007bff;
  border-radius: 25px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.view-more-btn:hover {
  background-color: #007bff;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

</style>
