<template>
  <div v-if="room" class="room-edit">
    <h1>방 정보 수정</h1>
    <form @submit.prevent="updateRoom" class="edit-form">
      <div class="form-group">
        <label for="theme_nm">테마 이름</label>
        <input id="theme_nm" v-model="room.THEME_NM" required>
      </div>

      <div class="form-group">
        <label for="theme_nm">장소</label>
        <input id="theme_nm" v-model="room.ROOM_NM" required>
      </div>
      
      <div class="form-group">
        <label for="rating">평점</label>
        <input id="rating" v-model.number="room.RATING" type="number" required min="0" max="5" step="0.1">
      </div>

      <div class="form-group">
        <label for="region">지역</label>
        <select id="region" v-model="room.REGION_CD" @change="handleRegionChange" required>
          <option disabled value="">선택하세요</option>
          <option v-for="region in regions" :key="region.CODE_VAL" :value="region.CODE_VAL">{{ region.CODE_NM }}</option>
        </select>
      </div>

      <div class="form-group">
        <label for="area">세부 지역</label>
        <select id="area" v-model="room.AREA_CD" required>
          <option disabled value="">선택하세요</option>
          <option v-for="area in areas" :key="area.CODE_VAL" :value="area.CODE_VAL">{{ area.CODE_NM }}</option>
        </select>
      </div>

      <div class="form-group">
        <label for="genre">장르</label>
        <select id="genre" v-model="room.GENRE_CD" required>
          <option disabled value="">선택하세요</option>
          <option v-for="genre in genres" :key="genre.CODE_VAL" :value="genre.CODE_VAL">{{ genre.CODE_NM }}</option>
        </select>
      </div>

      <div class="form-group">
        <label for="content">방 설명</label>
        <textarea id="content" v-model="room.CONTENT" rows="5" required></textarea>
      </div>

      <div class="form-group">
        <label for="min_party">최소 인원</label>
        <input id="min_party" v-model.number="room.MIN_PARTY" type="number" required min="1">
      </div>

      <div class="form-group">
        <label for="max_party">최대 인원</label>
        <input id="max_party" v-model.number="room.MAX_PARTY" type="number" required min="1">
      </div>

      <div class="form-group">
        <label for="run_time">소요 시간 (분)</label>
        <input id="run_time" v-model.number="room.RUN_TIME" type="number" required min="1">
      </div>

      <div class="form-group">
        <label for="activity">활동성 (1-5)</label>
        <select id="activity" v-model.number="room.ACTIVITY" required>
          <option disabled value="">선택하세요</option>
          <option v-for="value in [1, 2, 3, 4, 5]" :key="value" :value="value">{{ value }}</option>
        </select>
      </div>

      <div class="form-group">
        <label for="device_ratio">기기 활용도 (1-5)</label>
        <select id="device_ratio" v-model.number="room.DEVICE_RATIO" required>
          <option disabled value="">선택하세요</option>
          <option v-for="value in [1, 2, 3, 4, 5]" :key="value" :value="value">{{ value }}</option>
        </select>
      </div>

      <div class="form-group">
        <label for="difficulty">난이도 (1-5)</label>
        <select id="difficulty" v-model.number="room.LEVEL" required>
          <option disabled value="">선택하세요</option>
          <option v-for="value in [1, 2, 3, 4, 5]" :key="value" :value="value">{{ value }}</option>
        </select>
      </div>

      <div class="form-group">
        <label for="newTheme">새 테마</label>
        <div>
          <label>
            <input type="radio" v-model="room.NEW_THEME" value="Y"> Y
          </label>
          <label>
            <input type="radio" v-model="room.NEW_THEME" value="N"> N
          </label>
        </div>
      </div>

      <div class="form-group">
        <label for="rcmd">추천 테마</label>
        <div>
          <label>
            <input type="radio" v-model="room.RCMD" value="Y"> Y
          </label>
          <label>
            <input type="radio" v-model="room.RCMD" value="N"> N
          </label>
        </div>
      </div>

      <div class="form-group">
        <label for="price">가격 (원)</label>
        <input id="price" v-model.number="room.PRICE">
      </div>

      <div class="form-group">
        <label for="img_path">이미지 경로</label>
        <input id="img_path" v-model="room.IMG_PATH" required>
      </div>
      <div class="form-group">
        <label for="img_path">이미지 경로</label>
        <input id="img_path" v-model="room.SITE" required>
      </div>

      <div class="action-buttons">
        <button type="submit" class="btn btn-primary">수정 완료</button>
        <button type="button" class="btn btn-secondary" @click="cancel">취소</button>
      </div>
    </form>
  </div>
  <div v-else>
    로딩 중...
  </div>
</template>

<script>
import axios from 'axios';
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { mapGetters, useStore } from 'vuex';

export default {
  name: 'RoomEdit',
  setup() {
    const store = useStore();
    const route = useRoute();
    const router = useRouter();

    const room = ref(null);
    const regions = ref([]);
    const areas = ref([]);
    const genres = ref([]);

    const fetchRoomDetail = async () => {
      const roomId = route.params.id;
      try {
        const response = await axios.get(`/api/room/detail/${roomId}`);
        room.value = response.data;

        // 방의 초기 지역에 대해 세부 지역을 가져옵니다.
        if (room.value.REGION_CD) {
          await fetchAreas(room.value.REGION_CD);
        }
      } catch (error) {
        console.error('방 세부 정보를 가져오는 중 오류 발생:', error);
      }
    };

    const fetchRegions = async () => {
      const type = "REGION_CD";
      try {
        const response = await axios.get(`/api/common/code/${type}`);
        regions.value = response.data;
      } catch (error) {
        console.error('지역 정보를 가져오는 중 오류 발생:', error);
      }
    };
    
    const fetchAreas = async (regionCode) => {
      try {
        const response = await axios.get(`/api/common/areaCd/${regionCode}`);
        areas.value = response.data;
      } catch (error) {
        console.error('세부 지역 정보를 가져오는 중 오류 발생:', error);
      }
    };

    const fetchGenres = async () => {
      const type = "GENRE_CD"; // 장르에 대한 올바른 타입을 확인하세요
      try {
        const response = await axios.get(`/api/common/code/${type}`);
        genres.value = response.data;
      } catch (error) {
        console.error('장르 정보를 가져오는 중 오류 발생:', error);
      }
    };

    const updateRoom = async () => {
      
      try {
        const roomData = { ...room.value, userId: user.value.id };
        await axios.put(`/api/room/update/${room.value.ID}`, roomData);
        router.push(`/roomDetail/${room.value.ID}`);
      } catch (error) {
        console.error('방 정보 수정 중 오류 발생:', error);
      }
    };

    const cancel = () => {
      router.push(`/roomDetail/${room.value.ID}`);
    };

    const handleRegionChange = () => {
      fetchAreas(room.value.REGION_CD);
    };

    onMounted(() => {
      fetchRoomDetail();
      fetchRegions();
      fetchGenres();
    });
    const user = computed(() => {
        return store.getters.user;
    });

    return {
      room,
      regions,
      areas,
      genres,
      updateRoom,
      cancel,
      handleRegionChange,
      user,
    };
  },
  computed: {
      ...mapGetters(['user']) 
  },
};
</script>

<style scoped>
.room-edit {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
}

.edit-form {
  background-color: white;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}

.form-group {
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}

label {
  flex: 0 0 150px;
  margin-bottom: 5px;
  font-weight: bold;
}

input, select, textarea {
  flex: 1 1 calc(100% - 150px);
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
  box-sizing: border-box;
}

textarea {
  min-height: 100px;
  resize: vertical;
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.btn {
  padding: 10px 20px;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-left: 10px;
}

.btn-primary {
  background-color: #007bff;
  color: white;
  border: none;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
  border: none;
}

.btn:hover {
  opacity: 0.8;
}

@media (max-width: 600px) {
  .form-group {
    flex-direction: column;
    align-items: stretch;
  }

  label {
    flex: 0 0 auto;
    margin-bottom: 5px;
  }

  input, select, textarea {
    flex: 1 1 auto;
    width: 100%;
  }

  .action-buttons {
    flex-direction: column;
  }

  .btn {
    margin-left: 0;
    margin-top: 10px;
    width: 100%;
  }
}
</style>
