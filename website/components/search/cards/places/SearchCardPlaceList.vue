<template>
  <div>
    <h2 class="mt-24" v-if="card.title">{{card.title}}</h2>
    <p class="b-a75 text-ellipsis-2l" v-if="card.subtitle">{{card.subtitle}}</p>

    <horizontal-snap-view :card-width="cardWidth">
      <place-card class="ContentCard" v-for="place in card.places"
                  :place="place" :key="place.contentId" ref="contentCard"/>
    </horizontal-snap-view>
  </div>
</template>

<script>
  import ImageSizes from "../../../core/ImageSizes";
  import PlaceCard from "../../../places/PlaceCard";
  import HorizontalSnapView from "../../../core/HorizontalSnapView";

  export default {
    name: "SearchCardPlaceList",
    components: {HorizontalSnapView, PlaceCard, ImageSizes},
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        cardWidth: 300,
      }
    },
    mounted() {
      this.cardWidth = this.$refs.contentCard[0].$el.scrollWidth
    },
  }
</script>

<style scoped lang="less">
  .ContentCard {
    scroll-snap-align: start;
    width: calc(100vw - 24px);

    padding-left: 24px;
    margin-right: -8px;
  }

  @media (min-width: 420px) {
    .ContentCard {
      width: calc((100vw - 16px) / 2);
    }
  }

  @media (min-width: 792px) {
    .ContentCard {
      width: calc((100vw - 8px) / 3);
    }
  }

  @media (min-width: 1200px) {
    .ContentCard {
      padding: 0 12px;

      flex: 0 0 25%;
      max-width: 25%;
      margin: 0;
    }
  }

  @media (min-width: 1440px) {
    .ContentCard {
      flex: 0 0 20%;
      max-width: 20%;
    }
  }
</style>
