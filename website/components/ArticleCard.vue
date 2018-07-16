<template>
  <div>
    <div v-if="article">
      <img class="ImageCard" v-bind:src="article.thumbnail.original" width="369px" height="203px">
      <div class="ArticleContainer">
        <p class="WordCardTitle" word-wrap="">{{article.title}}</p>
        <p class="WordCard">{{article.description}}</p>
        <p class="AuthorName">By @biubiubiu</p>
      </div>
    </div>
  </div>
</template>

<script>
    export default {
        props: ["article"],
        name: "ArticleCard"
    }
</script>

<style>
  .ArticleContainer{
    position: relative;
    top: -160px;
    border-radius: 10px;
    background-color: #FFFFFF;
    box-shadow: 0 3px 6px 0 rgba(0,0,0,0.23), 0 3px 6px 0 rgba(0,0,0,0.16);
    height: 344px;
    width: 312px;
  }

  .ImageCard {
    position: relative;
    top: 8px;
    height: 168px;
    width: 312px;
    border-radius: 4px 4px 0 0;
    background-color: #D8D8D8;
    z-index: 100;
  }

  .WordCardTitle{
    position: relative;
    padding-top: 173px;
    left: 17px;
    color: #000000;
    font-family: "Open Sans";
    font-size: 19px;
    font-weight: 600;
    line-height: 26px;
    height: 200px;
    overflow: hidden;
  }

  .WordCard {
    position: relative;
    left: 17px;
    height: 88px;
    line-height: 22px;
    width: 256px;
    overflow: hidden;
  }

  .AuthorName {
    position: relative;
    left: 17px;
    height: 17px;
    width: 81px;
    color: #000000;
    font-family: "Open Sans";
    font-size: 12px;
    line-height: 17px;
  }
</style>
