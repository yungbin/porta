<template>
  <div class="contact-form">
    <h1>문의하기</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="type">문의유형</label>
        <select v-model="formData.type" id="type" required class="form-control">
          <option value="general">일반 문의</option>
          <option value="support">방탈출 리스트 추가요청</option>
          <option value="billing">제안하기</option>
          <!-- 추가적인 옵션이 필요하면 여기 추가 -->
        </select>
      </div>
      <div class="form-group">
        <label for="name">이름</label>
        <input type="text" v-model="formData.name" id="name" required class="form-control" />
      </div>
      <div class="form-group">
        <label for="email">이메일</label>
        <input type="email" v-model="formData.email" id="email" required class="form-control" />
      </div>
      <div class="form-group">
        <label for="phone">휴대폰번호</label>
        <input type="text" v-model="formData.phone" id="phone" required class="form-control" />
      </div>
      <div class="form-group">
        <label for="message">문의내용</label>
        <textarea v-model="formData.message" id="message" required class="form-control" rows="5"></textarea>
      </div>

      <button type="submit" class="btn btn-primary" :disabled="!isFormValid">전송</button>
    </form>

    <div v-if="successMessage" class="alert alert-success mt-3">{{ successMessage }}</div>
    <div v-if="errorMessage" class="alert alert-danger mt-3">{{ errorMessage }}</div>
  </div>
</template>

<script>
import axios from 'axios';
import { computed, ref, onMounted } from 'vue';
import { useStore } from 'vuex';

export default {
  name: 'ContactForm',
  setup() {
    const store = useStore();
    const user = computed(() => store.getters.user);
    const formData = ref({
      type: 'general',
      name: '',
      email: '',
      phone: '',
      message: '',
    });
    const successMessage = ref('');
    const errorMessage = ref('');

    onMounted(() => {
      formData.value.email = user.value.email || '';
      formData.value.name = user.value.username || '';
    });

    const isFormValid = computed(() => {
      return (
        formData.value.type &&
        formData.value.name &&
        formData.value.email &&
        formData.value.phone &&
        formData.value.message
      );
    });

    const submitForm = async () => {
      try {
        await axios.post('/api/contact', formData.value);
        successMessage.value = '문의가 성공적으로 전송되었습니다!';
        errorMessage.value = '';
        resetForm();
      } catch (error) {
        errorMessage.value = '문의 전송에 실패했습니다. 다시 시도해주세요.';
        successMessage.value = '';
      }
    };

    const resetForm = () => {
      formData.value = {
        type: '',
        name: user.value.username || '',
        email: '',
        phone: '',
        message: '',
      };
    };

    return {
      formData,
      submitForm,
      successMessage,
      errorMessage,
      isFormValid,
    };
  },
};
</script>

<style scoped>
.contact-form {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.form-group {
  margin-bottom: 15px;
}

.form-control {
  width: 100%;
  padding: 10px;
  border: 1px solid #ced4da;
  border-radius: 4px;
}

.btn {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.btn:hover {
  background-color: #0056b3;
}

.btn:disabled {
  background-color: #c0c0c0; /* 비활성화된 버튼 색상 */
  cursor: not-allowed;
}

.alert {
  margin-top: 15px;
  padding: 10px;
  border-radius: 5px;
}
</style>
