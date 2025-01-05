<template>
    <div>
      <h3>리뷰 목록</h3>
      <div v-for="review in reviews" :key="review.id" class="review-item">
        <p>{{ review.CONTENT }}</p>
        <span>{{ formatDate(review.PLAY_DATE) }}</span>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      reviews: Array
    },
    methods: {
      formatDate(date) {
        return new Date(date).toLocaleDateString();
      }
    }
  };
  </script>
  
  <style scoped>
  .review-item {
    border: 1px solid #ccc;
    margin: 10px 0;
    padding: 10px;
  }
  </style>
  