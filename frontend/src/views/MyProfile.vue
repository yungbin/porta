<template>
  <section class="profile-container">
    <div class="profile-header">
      <div class="profile-avatar">
        <img :src="profile.PROFILE_PICTURE" :alt="profile.USER_NM">
      </div>
      <h1>{{ profile.NICK_NM }}님의 프로필</h1>
      <p class="bio">{{ profile.BIO }}</p>
    </div>
    <div class="profile-content">
      <div class="profile-info">
        <div class="info-item">
          <i class="bi bi-person"></i>
          <span>ID: {{ profile.USER_ID }}</span>
        </div>
        <div class="info-item">
          <i class="bi bi-person"></i>
          <span>이름: {{ profile.USER_NM }}</span>
        </div>
        <div class="info-item">
          <i class="bi bi-cake2"></i>
          <span>생일: {{ profile.BIRTH }}</span>
        </div>
        <div class="info-item">
          <i class="bi bi-envelope"></i>
          <span>이메일: {{ profile.EMAIL }}</span>
        </div>
        <div class="info-item">
          <i class="bi bi-telephone"></i>
          <span>전화번호: {{ profile.PHONE }}</span>
        </div>
        <div class="info-item">
          <i class="bi bi-geo-alt"></i>
          <span>위치: {{ profile.ADDRESS }}</span>
        </div>
      </div>
    </div>
    <div class="profile-actions">
      <button class="edit-profile" @click="openEditModal">프로필 편집</button>
    </div>

    <div v-if="isEditModalVisible" class="modal" @click="closeEditModal">
      <div class="modal-content" @click.stop>
        <span class="close" @click="closeEditModal">&times;</span>
        <div class="profile-header" :style="{ backgroundImage: `url(${profile.PROFILE_PICTURE})` }">
          <div class="profile-avatar">
            <img :src="profile.PROFILE_PICTURE" :alt="profile.USER_NM">
          </div>
          <h1>{{ profile.USER_NM }}님의 프로필 편집</h1>
        </div>
        <div class="profile-details">
          <div class="form-group">
            <label>UserId : {{ profile.USER_ID }}</label>
          </div>
          <div class="form-group">
            <label>NickName:</label>
            <input v-model="profile.NICK_NM">
          </div>
          <div class="form-group">
            <label>Birth:</label>
            <input v-model="profile.BIRTH" type="date">
          </div>
          <div class="form-group">
            <label>Email : {{ profile.EMAIL }}</label>
          </div>
          <div class="form-group">
            <label>Phone:</label>
            <input v-model="profile.PHONE">
          </div>
          <div class="form-group">
            <label>Address:</label>
            <input v-model="profile.ADDRESS">
          </div>
        </div>
        <div class="button-container">
          <button class="btn btn-primary" @click="updateProfile">Update</button>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import axios from 'axios';

export default {
  name: 'UserProfile',
  data() {
    return {
      profile: {},
      isEditModalVisible: false
    };
  },
  created() {
    this.fetchProfileData();
  },
  methods: {
    fetchProfileData() {
      axios.get('api/member/profile')
        .then(response => {
          this.profile = response.data;
          console.log(this.profile);
        })
        .catch(error => {
          console.error('Error fetching profile data:', error);
        });
    },
    openEditModal() {
      this.isEditModalVisible = true; // 모달 표시
    },
    closeEditModal() {
      this.isEditModalVisible = false; // 모달 숨기기
    },
    updateProfile() {
      console.log(this.profile);
      axios.put('api/member/profile', this.profile)
        .then(response => {
          console.log('Profile updated:', response.data);
          this.fetchProfileData(); // 업데이트된 프로필 다시 가져오기
          this.closeEditModal(); // 모달 숨기기
        })
        .catch(error => {
          console.error('Error updating profile:', error);
        });
    }
  }
};
</script>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 2rem auto;
  background-color: #ffffff;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.profile-header {
  background: linear-gradient(135deg, #3498db, #8e44ad);
  color: white;
  padding: 2rem;
  text-align: center;
}

.profile-avatar {
  width: 150px;
  height: 150px;
  margin: 0 auto 1rem;
  border-radius: 50%;
  overflow: hidden;
  border: 5px solid white;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.profile-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-header h1 {
  font-size: 2rem;
  margin: 0 0 0.5rem;
}

.bio {
  font-style: italic;
  margin-bottom: 0;
}

.profile-content {
  padding: 2rem;
}

.profile-info {
  display: grid;
  gap: 1rem;
}

.info-item {
  display: flex;
  align-items: center;
  font-size: 1.1rem;
  color: #333;
}

.info-item i {
  margin-right: 1rem;
  font-size: 1.3rem;
  color: #3498db;
}

.info-item a {
  color: #3498db;
  text-decoration: none;
}

.info-item a:hover {
  text-decoration: underline;
}

.profile-actions {
  padding: 0 2rem 2rem;
  text-align: center;
}

.edit-profile {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 0.8rem 2rem;
  font-size: 1rem;
  border-radius: 25px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.edit-profile:hover {
  background-color: #2980b9;
}
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  z-index: 1001;
  width: 90%;
  max-width: 500px;
}

.close {
  cursor: pointer;
  float: right;
  font-size: 1.5rem;
}

.profile-details {
  margin-top: 20px;
}


.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #495057;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ced4da;
  border-radius: 4px;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #80bdff;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

.button-container {
  display: flex;
  justify-content: center;
  margin-top: 20px; /* 필요에 따라 여백 조정 */
  margin-bottom: 20px; /* 필요에 따라 여백 조정 */
}

.text-center {
  text-align: center !important;
}

.input-group {
  display: flex;
  gap: 10px; /* 셀 간격 조정 */
}

.btn {
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s;
}

.btn-info {
  background-color: #17a2b8;
  color: white;
}

.btn-danger {
  background-color: #dc3545;
  color: white;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn:hover {
  opacity: 0.8;
}
</style>