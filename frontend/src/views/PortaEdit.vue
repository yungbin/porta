<template>
  <div v-if="porta" class="porta-edit">
    <h1>방 정보 수정</h1>
    <form @submit.prevent="updatePorta" class="edit-form">
      <div class="form-group">
        <label for="theme_nm">테마 이름</label>
        <input id="theme_nm" v-model="porta.THEME_NM" required>
      </div>

      <div class="form-group">
        <label for="theme_nm">장소</label>
        <input id="theme_nm" v-model="porta.PORTA_NM" required>
      </div>

      <div class="form-group">
        <label for="rating">평점</label>
        <input id="rating" v-model.number="porta.RATING" type="number" required min="0" max="5" step="0.1">
      </div>

      <div class="form-group">
        <label for="region">지역</label>
        <select id="region" v-model="porta.REGION_CD" @change="handleRegionChange" required>
          <option disabled value="">선택하세요</option>
          <option v-for="region in regions" :key="region.CODE_VAL" :value="region.CODE_VAL">{{ region.CODE_NM }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="area">세부 지역</label>
        <select id="area" v-model="porta.AREA_CD" required>
          <option disabled value="">선택하세요</option>
          <option v-for="area in areas" :key="area.CODE_VAL" :value="area.CODE_VAL">{{ area.CODE_NM }}</option>
        </select>
      </div>

      <div class="form-group">
        <label for="genre">장르</label>
        <select id="genre" v-model="porta.GENRE_CD" required>
          <option disabled value="">선택하세요</option>
          <option v-for="genre in genres" :key="genre.CODE_VAL" :value="genre.CODE_VAL">{{ genre.CODE_NM }}</option>
        </select>
      </div>

      <div class="form-group">
        <label for="content">방 설명</label>
        <textarea id="content" v-model="porta.CONTENT" rows="5" required></textarea>
      </div>

      <div class="form-group">
        <label for="min_party">최소 인원</label>
        <input id="min_party" v-model.number="porta.MIN_PARTY" type="number" required min="1">
      </div>

      <div class="form-group">
        <label for="max_party">최대 인원</label>
        <input id="max_party" v-model.number="porta.MAX_PARTY" type="number" required min="1">
      </div>

      <div class="form-group">
        <label for="run_time">소요 시간 (분)</label>
        <input id="run_time" v-model.number="porta.RUN_TIME" type="number" required min="1">
      </div>

      <div class="form-group">
        <label for="activity">활동성 (1-5)</label>
        <select id="activity" v-model.number="porta.ACTIVITY" required>
          <option disabled value="">선택하세요</option>
          <option v-for="value in [1, 2, 3, 4, 5]" :key="value" :value="value">{{ value }}</option>
        </select>
      </div>

      <div class="form-group">
        <label for="device_ratio">기기 활용도 (1-5)</label>
        <select id="device_ratio" v-model.number="porta.DEVICE_RATIO" required>
          <option disabled value="">선택하세요</option>
          <option v-for="value in [1, 2, 3, 4, 5]" :key="value" :value="value">{{ value }}</option>
        </select>
      </div>

      <div class="form-group">
        <label for="difficulty">난이도 (1-5)</label>
        <select id="difficulty" v-model.number="porta.LEVEL" required>
          <option disabled value="">선택하세요</option>
          <option v-for="value in [1, 2, 3, 4, 5]" :key="value" :value="value">{{ value }}</option>
        </select>
      </div>

      <div class="form-group">
        <label for="newTheme">새 테마</label>
        <div>
          <label>
            <input type="radio" v-model="porta.NEW_THEME" value="Y"> Y
          </label>
          <label>
            <input type="radio" v-model="porta.NEW_THEME" value="N"> N
          </label>
        </div>
      </div>

      <div class="form-group">
        <label for="rcmd">추천 테마</label>
        <div>
          <label>
            <input type="radio" v-model="porta.RCMD" value="Y"> Y
          </label>
          <label>
            <input type="radio" v-model="porta.RCMD" value="N"> N
          </label>
        </div>
      </div>

      <div class="form-group">
        <label for="price">가격 (원)</label>
        <input id="price" v-model.number="porta.PRICE">
      </div>

      <div class="form-group">
        <label for="img_path">이미지 경로</label>
        <input id="img_path" v-model="porta.IMG_PATH" required>
      </div>
      <div class="form-group">
        <label for="img_path">이미지 경로</label>
        <input id="img_path" v-model="porta.SITE" required>
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
  name: 'PortaEdit',
  setup() {
    const store = useStore();
    const route = useRoute();
    const router = useRouter();

    const porta = ref(null);
    const regions = ref([]);
    const areas = ref([]);
    const genres = ref([]);

    const fetchPortaDetail = async () => {
      const portaId = route.params.id;
      try {
        const response = await axios.get(`/api/porta/detail/${portaId}`);
        porta.value = response.data;

        // 방의 초기 지역에 대해 세부 지역을 가져옵니다.
        if (porta.value.REGION_CD) {
          await fetchAreas(porta.value.REGION_CD);
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

    const updatePorta = async () => {

      try {
        const portaData = { ...porta.value, userId: user.value.id };
        await axios.put(`/api/porta/update/${porta.value.ID}`, portaData);
        router.push(`/portaDetail/${porta.value.ID}`);
      } catch (error) {
        console.error('방 정보 수정 중 오류 발생:', error);
      }
    };

    const cancel = () => {
      router.push(`/portaDetail/${porta.value.ID}`);
    };

    const handleRegionChange = () => {
      fetchAreas(porta.value.REGION_CD);
    };

    onMounted(() => {
      fetchPortaDetail();
      fetchRegions();
      fetchGenres();
    });
    const user = computed(() => {
      return store.getters.user;
    });

    return {
      porta,
      regions,
      areas,
      genres,
      updatePorta,
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
.porta-edit {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
}

.edit-form {
  background-color: white;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
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

input,
select,
textarea {
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

  input,
  select,
  textarea {
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
