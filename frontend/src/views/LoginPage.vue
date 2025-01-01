<template>
    <div id="login-page">
        <LoginForm @login-success="loginSuccess" />
    </div>
</template>
  
<script>
import LoginForm from '../components/LoginForm.vue';

export default {
    components: {
        LoginForm
    },
    methods: {
        loginSuccess() {
            this.$router.push('/');
        }
    }
}
</script>
  
<style>
#login-page {
    display: flex;
    justify-content: center;
    align-items: center;
    height: auto;
}
</style>
  