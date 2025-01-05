<template>
  <div class="member-list-container">
    <h2 class="title">Member List</h2>
    <div class="table-responsive">
      <table class="member-table">
        <thead>
          <tr>
            <th>UserId</th>
            <th>Username</th>
            <th>Email</th>
            <th class="text-center">USE</th>
            <th class="text-center">Role</th>
            <th class="text-center">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="member in members" :key="member.ID">
            <td>{{ member.USER_ID }}</td>
            <td>{{ member.USER_NM }}</td>
            <td>{{ member.EMAIL }}</td>
            <td class="text-center">{{ member.USE_YN }}</td>
            <td class="text-center"><span :class="['role-badge', member.ROLE.toLowerCase()]">{{ member.ROLE }}</span>
            </td>
            <td class="text-center">
              <button class="btn btn-info" @click="showInfo(member)">Info</button>
              <button class="btn btn-danger" @click="deleteMember(member.USER_ID)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="selectedMember" class="modal" @click="closeModal">
      <div class="modal-content" @click.stop>
        <span class="close" @click="closeModal">&times;</span>
        <div class="profile-header" :style="{ backgroundImage: `url(${selectedMember.PROFILE_PICTURE})` }">
          <div class="profile-avatar">
            <img :src="selectedMember.PROFILE_PICTURE" :alt="selectedMember.USER_NM">
          </div>
          <h1>{{ selectedMember.USER_NM }}님의 프로필</h1>
          <p class="bio">{{ selectedMember.BIO }}</p>
        </div>
        <div class="profile-details">
          <div class="form-group">
            <label>UserId : {{ selectedMember.USER_ID }}</label>
          </div>
          <div class="form-group">
            <label>Username:</label>
            <input v-model="selectedMember.USER_NM">
          </div>
          <div class="form-group">
            <label>Phone:</label>
            <input v-model="selectedMember.PHONE">
          </div>
          <div class="form-group">
            <label>Email:</label>
            <input v-model="selectedMember.EMAIL">
          </div>
          <div class="form-group">
            <label>Birth:</label>
            <div class="input-group">
              <select v-model="selectedYear" @change="updateDays">
                <option value="">연도 선택</option>
                <option v-for="year in years" :key="year" :value="year">{{ year }}년</option>
              </select>
              <select v-model="selectedMonth" @change="updateDays">
                <option value="">월 선택</option>
                <option v-for="month in months" :key="month" :value="month">{{ month }}월</option>
              </select>
              <select v-model="selectedDay">
                <option value="">일 선택</option>
                <option v-for="day in days" :key="day" :value="day">{{ day }}일</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label>Role:</label>
            <select v-model="selectedMember.ROLE">
              <option value="ADMIN">ADMIN</option>
              <option value="USER">USER</option>
            </select>
          </div>
          <div class="form-group">
            <label>Address:</label>
            <input v-model="selectedMember.ADDRESS">
          </div>
          <div class="form-group">
            <label>Use:</label>
            <select v-model="selectedMember.USE_YN">
              <option value="Y">USE</option>
              <option value="N">DELETE</option>
            </select>
          </div>
        </div>
        <div class="button-container">
          <button class="btn btn-primary" @click="updateMember(selectedMember)">Update</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  name: 'MemberList',
  setup() {
    const members = ref([]);
    const selectedMember = ref(null);

    // 생년월일 선택을 위한 상태 변수
    const selectedYear = ref('');
    const selectedMonth = ref('');
    const selectedDay = ref('');
    const years = Array.from({ length: 100 }, (_, i) => new Date().getFullYear() - i);
    const months = Array.from({ length: 12 }, (_, i) => i + 1);
    const days = ref([]);

    const fetchMembers = async () => {
      try {
        const response = await axios.get('/api/member/list');
        members.value = response.data;
      } catch (error) {
        console.error('Error fetching members:', error);
      }
    };

    const deleteMember = async (userId) => {
      if (confirm('Are you sure you want to delete this member?')) {
        try {
          const response = await axios.delete(`/api/member/delete/${userId}`);
          if (response.data.status === 'deleted') {
            alert('Member deleted successfully');
            fetchMembers();
          } else {
            alert('Failed to delete member');
          }
        } catch (error) {
          console.error('Error deleting member:', error);
        }
      }
    };

    const updateMember = async (member) => {
      try {
        const birthDate = `${selectedYear.value}-${String(selectedMonth.value).padStart(2, '0')}-${String(selectedDay.value).padStart(2, '0')}`; // 날짜 포맷
        const response = await axios.put(`/api/member/update/${member.USER_ID}`, {
          username: member.USER_NM,
          nickname: member.NICK_NM,
          phone: member.PHONE,
          email: member.EMAIL,
          birth: birthDate,
          role: member.ROLE,
          address: member.ADDRESS,
          use: member.USE_YN,
        });
        selectedMember.value.BIRTH = birthDate;

        if (response.data.status === 'updated') {
          alert('Member updated successfully');
          closeModal();
          fetchMembers();
        } else {
          alert('Failed to update member');
        }
      } catch (error) {
        console.error('Error updating member:', error);
      }
    };

    const updateDays = () => {
      if (selectedYear.value && selectedMonth.value) {
        const daysInMonth = new Date(selectedYear.value, selectedMonth.value, 0).getDate();
        days.value = Array.from({ length: daysInMonth }, (_, i) => i + 1);
      } else {
        days.value = [];
      }
    };

    const showInfo = (member) => {
      selectedMember.value = { ...member };
      selectedYear.value = member.BIRTH ? new Date(member.BIRTH).getFullYear() : '';
      selectedMonth.value = member.BIRTH ? new Date(member.BIRTH).getMonth() + 1 : '';
      selectedDay.value = member.BIRTH ? new Date(member.BIRTH).getDate() : '';
      updateDays(); // 생년월일 선택을 위한 일 수 업데이트
    };

    const closeModal = () => {
      selectedMember.value = null;
      selectedYear.value = '';
      selectedMonth.value = '';
      selectedDay.value = '';
      days.value = [];
    };

    onMounted(fetchMembers);

    return {
      members,
      deleteMember,
      updateMember,
      showInfo,
      selectedMember,
      closeModal,
      selectedYear,
      selectedMonth,
      selectedDay,
      years,
      months,
      days,
      updateDays,
    };
  },
};
</script>

<style scoped>
.member-list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.title {
  color: #333;
  text-align: center;
  margin-bottom: 20px;
}

.table-responsive {
  overflow-x: auto;
}

.member-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  background-color: #fff;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  overflow: hidden;
}

.member-table th,
.member-table td {
  padding: 15px;
  text-align: left;
}

.member-table th {
  background-color: #f8f9fa;
  color: #495057;
  font-weight: bold;
  text-transform: uppercase;
}

.member-table tr:nth-child(even) {
  background-color: #f8f9fa;
}

.member-table tr:hover {
  background-color: #e9ecef;
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

.role-badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.8em;
  font-weight: bold;
}

.role-badge.admin {
  background-color: #ffc107;
  color: #212529;
}

.role-badge.user {
  background-color: #28a745;
  color: white;
}

.modal {
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 500px;
  max-width: 90%;
  max-height: 90%;
  overflow-y: auto;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
}

.close:hover {
  color: #000;
}

.profile-header {
  background-size: cover;
  background-position: center;
  color: white;
  padding: 30px;
  text-align: center;
  position: relative;
}

.profile-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
}

.profile-avatar {
  width: 120px;
  height: 120px;
  margin: 0 auto 1rem;
  border-radius: 50%;
  overflow: hidden;
  border: 4px solid white;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  position: relative;
  z-index: 1;
}

.profile-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-header h1 {
  font-size: 2rem;
  margin: 0 0 0.5rem;
  position: relative;
  z-index: 1;
}

.bio {
  font-style: italic;
  margin-bottom: 0;
  position: relative;
  z-index: 1;
}

.profile-details {
  padding: 20px;
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
  margin-top: 20px;
  /* 필요에 따라 여백 조정 */
  margin-bottom: 20px;
  /* 필요에 따라 여백 조정 */
}

.text-center {
  text-align: center !important;
}

.input-group {
  display: flex;
  gap: 10px;
  /* 셀 간격 조정 */
}
</style>