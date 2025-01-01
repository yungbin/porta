<template>
    <div id="regester-page">
        <Regester @regester-success="regesterSuccess" />
    </div>
</template>
  
<script>
import Regester from '../components/Register.vue';

export default {
    components: {
        Regester
    },
    methods: {
        regesterSuccess() {
            this.$router.push('/');
        }
    }
}
</script>
  
<style>
#regester-page {
    display: flex;
    justify-content: center;
    align-items: center;
    height: auto;
}
</style>
  