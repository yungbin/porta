<template>
  <div class="porta-create">
    <h1 class="title">방 정보 등록</h1>
    <form @submit.prevent="savePorta" class="create-form">
      <div class="form-row">
        <div class="form-group">
          <label for="theme_nm">테마 이름</label>
          <input id="theme_nm" v-model="porta.THEME_NM" required>
        </div>
        <div class="form-group">
          <label for="porta_nm">장소</label>
          <input id="porta_nm" v-model="porta.PORTA_NM" required>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label for="theme_nm">SITE</label>
          <input id="theme_nm" v-model="porta.SITE" required>
        </div>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label for="newTheme">추천 테마</label>
          <div class="radio-group">
            <label class="radio-label">
              <input type="radio" v-model="porta.RCMD" value="Y" id="rcmdY">
              <span class="radio-custom"></span>
              Y
            </label>
            <label class="radio-label">
              <input type="radio" v-model="porta.RCMD" value="N" id="rcmdN">
              <span class="radio-custom"></span>
              N
            </label>
          </div>
        </div>
        <div class="form-group">
          <label for="newTheme">새 테마</label>
          <div class="radio-group">
            <label class="radio-label">
              <input type="radio" v-model="porta.NEW_THEME" value="Y" id="newThemeY">
              <span class="radio-custom"></span>
              Y
            </label>
            <label class="radio-label">
              <input type="radio" v-model="porta.NEW_THEME" value="N" id="newThemeN">
              <span class="radio-custom"></span>
              N
            </label>
          </div>
        </div>
      </div>
      <div class="form-row">
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
      </div>
      <div class="form-row">
        <div class="form-group">
          <label for="genre">장르</label>
          <select id="genre" v-model="porta.GENRE_CD" required>
            <option disabled value="">선택하세요</option>
            <option v-for="genre in genres" :key="genre.CODE_VAL" :value="genre.CODE_VAL">{{ genre.CODE_NM }}</option>
          </select>
        </div>
      </div>

      <div class="form-group full-width">
        <label for="content">방 설명</label>
        <textarea id="content" v-model="porta.CONTENT" rows="5" required></textarea>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label for="min_party">최소 인원</label>
          <input id="min_party" v-model.number="porta.MIN_PARTY" type="number" required min="1">
        </div>
        <div class="form-group">
          <label for="max_party">최대 인원</label>
          <input id="max_party" v-model.number="porta.MAX_PARTY" type="number" required min="1">
        </div>
      </div>

      <div class="form-row">
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
      </div>

      <div class="form-row">
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
      </div>

      <div class="form-row">
        <div class="form-group">
          <label for="rating">평점</label>
          <input id="rating" v-model.number="porta.RATING" type="number" required min="0" max="5" step="0.1">
        </div>
        <div class="form-group">
          <label for="price">가격 (원)</label>
          <input id="price" v-model.number="porta.PRICE" required>
        </div>
      </div>

      <div class="action-buttons">
        <button type="submit" class="btn btn-primary">등록</button>
        <button type="button" class="btn btn-secondary" @click="cancel">취소</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { mapGetters, useStore } from 'vuex';

export default {
  name: 'PortaCreate',
  setup() {
    const store = useStore();
    const router = useRouter();

    const porta = ref({
      THEME_NM: '',
      PORTA_NM: '',
      SITE: '',
      RATING: 0,
      REGION_CD: '',
      AREA_CD: '',
      GENRE_CD: '',
      CONTENT: '',
      MIN_PARTY: 2,
      MAX_PARTY: 4,
      RUN_TIME: 70,
      ACTIVITY: 1,
      DEVICE_RATIO: 1,
      LEVEL: 1,
      RCMD: 'N',
      NEW_THEME: 'N',
      PRICE: '인당 20000',
      IMG_PATH: ''
    });

    const regions = ref([]);
    const areas = ref([]);
    const genres = ref([]);

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
      const type = "GENRE_CD";
      try {
        const response = await axios.get(`/api/common/code/${type}`);
        genres.value = response.data;
      } catch (error) {
        console.error('장르 정보를 가져오는 중 오류 발생:', error);
      }
    };

    const savePorta = async () => {
      try {
        porta.value.IMG_PATH = `/poster/${porta.value.THEME_NM}.jpg`;
        const portaData = { ...porta.value, userId: user.value.id };
        await axios.post(`/api/porta/submit`, portaData);
        router.push(`/portaList`);
      } catch (error) {
        console.error('방 정보 저장 중 오류 발생:', error);
      }
    };

    const cancel = () => {
      router.push(`/portaList`);
    };

    const handleRegionChange = () => {
      fetchAreas(porta.value.REGION_CD);
    };

    onMounted(() => {
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
      savePorta,
      cancel,
      handleRegionChange,
      user
    };
  },
  computed: {
    ...mapGetters(['user'])
  },
};
</script>

<style scoped>
.porta-create {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
  font-family: 'Arial', sans-serif;
}

.title {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.create-form {
  background-color: #fff;
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.form-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.form-group {
  flex: 0 0 48%;
}

.full-width {
  flex: 0 0 100%;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #555;
}

input,
select,
textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
  transition: border-color 0.3s;
}

input:focus,
select:focus,
textarea:focus {
  outline: none;
  border-color: #007bff;
}

textarea {
  min-height: 100px;
  resize: vertical;
}

.radio-group {
  display: flex;
  gap: 20px;
}

.radio-label {
  display: flex;
  align-items: center;
  cursor: pointer;
  user-select: none;
}

.radio-label input[type="radio"] {
  position: absolute;
  opacity: 0;
  cursor: pointer;
}

.radio-custom {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid #007bff;
  border-radius: 50%;
  margin-right: 10px;
  position: relative;
}

.radio-custom::after {
  content: '';
  position: absolute;
  display: none;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #007bff;
}

.radio-label input[type="radio"]:checked+.radio-custom::after {
  display: block;
}

.radio-label:hover input[type="radio"]~.radio-custom {
  background-color: #f0f0f0;
}

.radio-label input[type="radio"]:checked~.radio-custom {
  border-color: #007bff;
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  margin-top: 30px;
}

.btn {
  padding: 12px 24px;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.1s;
  border: none;
  margin-left: 10px;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn:hover {
  opacity: 0.9;
  transform: translateY(-2px);
}

@media (max-width: 600px) {
  .form-row {
    flex-direction: column;
  }

  .form-group {
    flex: 0 0 100%;
    margin-bottom: 20px;
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

input,
select,
textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
  transition: border-color 0.3s;
  box-sizing: border-box;
  /* 패딩과 보더를 폭에 포함시킴 */
  -webkit-appearance: none;
  /* 브라우저 기본 스타일 제거 */
  -moz-appearance: none;
  appearance: none;
}

input:focus,
select:focus,
textarea:focus {
  outline: none;
  border-color: #007bff;
}

.form-group select {
  /* select 요소의 폭을 일관되게 만듬 */
  width: 100%;
  padding: 10px;
}
</style>