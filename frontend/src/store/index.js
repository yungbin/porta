import { createStore } from 'vuex';

function parseJSON(value) {
  try {
    return JSON.parse(value);
  } catch (e) {
    return null;
  }
}

export default createStore({
  state: {
    user: parseJSON(sessionStorage.getItem('user')) || null,
    isAuthenticated: !!sessionStorage.getItem('user'),
    role: sessionStorage.getItem('role') || ''
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user;
      state.isAuthenticated = !!user;
      sessionStorage.setItem('user', JSON.stringify(user));
    },
    SET_ROLE(state, role) {
      state.role = role;
      sessionStorage.setItem('role', role);
    },
    LOGOUT(state) {
      console.log('Logging out...');
      state.user = null;
      state.isAuthenticated = false;
      state.role = '';
      localStorage.removeItem('user');
      localStorage.removeItem('role');
      sessionStorage.removeItem('user');
      sessionStorage.removeItem('role');
    }
  },
  actions: {
    login({ commit }, { user, role }) {
      commit('SET_USER', user);
      commit('SET_ROLE', role);
    },
    logout({ commit }) {
      commit('LOGOUT');
    }
  },
  getters: {
    isAuthenticated(state) {
      return state.isAuthenticated;
    },
    user(state) {
      return state.user;
    },
    role(state) {
      return state.role;
    }
  }
});
