<template>
  <div class="PlaceAwardList">
    <slick class="Slick" ref="slick" :options="options">
      <div v-for="award in awards" :key="award.itemId">
        <div class="Award no-select whisper-100-bg">
          <div>{{award.award.name}}</div>
        </div>
      </div>
    </slick>
  </div>
</template>

<script>
  import Slick from 'vue-slick';
  import "slick-carousel/slick/slick.css";

  export default {
    name: "PlaceAwardList",
    components: {Slick},
    props: {
      awards: {
        type: Array,
        required: true
      }
    },
    data() {
      return {
        options: {
          mobileFirst : true,
          speed: 300,
          infinite: false,
          slidesToShow: 1,
          variableWidth: true,
          centerMode: false,
          arrows: false,
          slidesToScroll: 1,
          dots: false,
          responsive: [
            {
              breakpoint: 1200,
              settings: {
                slidesToScroll: 3,
              }
            },
            {
              breakpoint: 992,
              settings: {
                slidesToScroll: 2,
              }
            },
            {
              breakpoint: 576,
              settings: {
                slidesToScroll: 1,
              }
            }
          ]
        },
      }
    },
  }
</script>

<style scoped lang="less">
  .Award {
    width: 172px;
    height: 48px;
    padding: 0 8px;
    border-radius: 3px;
    margin-right: 8px;

    > div {
      max-height: 40px;

      color: rgba(0, 0, 0, 0.8);
      font-size: 13px;
      font-weight: 600;
      line-height: 1.25;
      text-align: center;

      -webkit-line-clamp: 2;
      overflow: hidden;
      display: -webkit-box;
      -webkit-box-orient: vertical;
    }
  }

  .Slick {
    display: flex;
  }

  .Award {
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
