<template>
  <div class="ArticleRow">
    <b-row>
      <b-col cols="12" md="4" v-for="index in 3" :key="index" class="d-flex align-items-stretch">
        <article-card v-bind:article="articles[index-1]"></article-card>
      </b-col>
    </b-row>
  </div>
</template>

<script>
    import ArticleCard from '~/components/ArticleCard.vue'
    export default {
        props: ["articles"],
        name: "ArticleRow",
        components: {
          ArticleCard
        }
    }
</script>

<style>
  .ArticleRow{
    min-width:1000px;
  }
</style>
