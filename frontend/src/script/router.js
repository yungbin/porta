import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/views/Home.vue';
import LoginForm from '@/views/LoginPage.vue';
import PortaList from '@/views/PortaList.vue';
import MyLikes from '@/views/MyLikes.vue';
import MyRecords from '@/views/MyRecords.vue';
import PortaSubmit from '@/views/PortaSubmit.vue';
import PortaDetail from '@/views/PortaDetail.vue';
import PortaEdit from '@/views/PortaEdit.vue';
import NewLinkList from '@/views/NewLinkList.vue';
import RcmdThemeList from '@/views/RcmdThemeList.vue';
import PortaRank from '@/views/PortaRank.vue';
import NoticeList from '@/views/NoticeList.vue';
import NoticeDetail from '@/views/NoticeDetail.vue';
import NoticeEdit from '@/views/NoticeEdit.vue';
import NoticeCreate from '@/views/NoticeCreate.vue';
import Register from '@/views/RegisterPage.vue';
import MemberList from '@/views/MemberList.vue';
import MyProfile from '@/views/MyProfile.vue';

const routes = [
  { path: '/', name: 'Home', component: Home, meta: { title: 'Porta' } },
  { path: '/loginForm', name: 'LoginForm', component: LoginForm, meta: { title: '로그인' } },
  { path: '/portaList', name: 'PortaList', component: PortaList, meta: { title: '방탈출 목록' } },
  { path: '/myLikes', name: 'MyLikes', component: MyLikes, meta: { title: '나의 좋아요' } },
  { path: '/myRecords', name: 'MyRecords', component: MyRecords, meta: { title: '나의 기록' } },
  { path: '/portaSubmit', name: 'PortaSubmit', component: PortaSubmit, meta: { requiresAuth: true, admin: true, title: '방탈출 정보 등록' } },
  { path: '/portaDetail/:id', name: 'PortaDetail', component: PortaDetail, meta: { title: '방탈출 상세 정보' } },
  { path: '/portaEdit/:id', name: 'PortaEdit', component: PortaEdit, meta: { requiresAuth: true, admin: true, title: '방탈출 정보 수정' } },
  { path: '/portaRank', name: 'PortaRank', component: PortaRank, meta: { title: '방탈출 평점 순위' } },
  { path: '/newLinkList', name: 'NewLinkList', component: NewLinkList, meta: { title: '새로운 테마 목록' } },
  { path: '/rcmdThemeList', name: 'RcmdThemeList', component: RcmdThemeList, meta: { title: '추천 테마 목록' } },
  { path: '/noticeList', name: 'NoticeList', component: NoticeList, meta: { title: '공지 목록' } },
  { path: '/noticeDetail/:id', name: 'NoticeDetail', component: NoticeDetail, meta: { title: '공지 상세 정보' } },
  { path: '/noticeEdit/:id', name: 'NoticeEdit', component: NoticeEdit, meta: { requiresAuth: true, admin: true, title: '공지 수정' } },
  { path: '/noticeCreate', name: 'NoticeCreate', component: NoticeCreate, meta: { requiresAuth: true, admin: true, title: '공지 등록' } },
  { path: '/register', name: 'Register', component: Register, meta: { title: '회원가입' } },
  { path: '/memberList', name: 'MemberList', component: MemberList, meta: { requiresAuth: true, admin: true, title: '회원 목록' } },
  { path: '/myProfile', name: 'MyProfile', component: MyProfile, meta: { title: '내 프로필' } },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

// router.beforeEach((to, from, next) => {
//   const user = JSON.parse(sessionStorage.getItem('user'));
//   const isAuthenticated = !!user;
//   const role = sessionStorage.getItem('role');

//   if (to.meta.requiresAuth && !isAuthenticated) {
//     console.log(to.meta.requiresAuth);
//     next('/loginForm'); 
//   } else if (to.meta.admin && role !== 'ADMIN') {
//     alert('접근할 수 있는 권한이 아닙니다'); 
//     next('/'); 
//   } else {
//     next();
//   }
// });

router.afterEach((to) => {
  document.title = to.meta.title || '기본 제목'; // 기본 제목을 설정
});

export default router;
