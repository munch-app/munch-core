<template>
  <div>
    <h2 class="mt-24">{{card.series.title}}</h2>
    <p class="b-a75 text-ellipsis-2l">{{card.series.subtitle}}</p>

    <div class="relative" ref="container">
      <div class="ArrowButton flex-align-center absolute wh-100">
        <div @click="prev" v-if="hasPrev"
             class="Control Prev elevation-1 relative index-navigation hover-pointer"/>
        <div @click="next" v-if="hasNext"
             class="Control Next elevation-1 relative index-navigation hover-pointer"/>
      </div>


      <div class="ContentListParent mtb-24" :class="expandType">
        <div class="ContentList flex relative" ref="scrollable">
          <search-series-content-card class="flex-no-shrink ContentCard" v-for="content in card.contents"
                                      :content="content" :key="content.contentId"
                                      :options="card.options"
                                      ref="contentCard"/>
          <div>
            <div class="gutter"/>
          </div>
          <div>
            <div class="gutter"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ImageSizes from "../../../core/ImageSizes";
  import SearchSeriesContentCard from "./SearchSeriesContentCard";

  export default {
    name: "SearchCardSeriesList",
    components: {SearchSeriesContentCard, ImageSizes},
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        hasNext: true,
        hasPrev: false,
        cardWidth: 300,
      }
    },
    computed: {
      expandType() {
        const expand = this.card.options && this.card.options.expand
        if (expand === 'height') {
          return 'ExpandHeight'
        }
        return 'ExpandWidth'
      }
    },
    mounted() {
      this.cardWidth = this.$refs.contentCard[0].$el.scrollWidth

      this.notifyButton(this.$refs.scrollable.scrollLeft)
    },
    methods: {
      prev() {
        let scrollable = this.$refs.scrollable
        let scrollLeft = scrollable.scrollLeft - this.cardWidth
        scrollable.scrollLeft = scrollLeft

        this.notifyButton({scrollLeft})
      },
      next() {
        let scrollable = this.$refs.scrollable
        let scrollLeft = scrollable.scrollLeft + this.cardWidth
        scrollable.scrollLeft = scrollLeft

        this.notifyButton({scrollLeft})
      },
      notifyButton({scrollLeft}) {
        this.hasPrev = scrollLeft > 0
      }
    }
  }
</script>

<style scoped lang="less">
  .ContentListParent {
    overflow-y: hidden;

    margin-left: -24px;
    margin-right: -24px;
    margin-bottom: -24px;
    width: calc(100% + 48px);
    height: 100%;
  }

  .ContentList {
    padding-bottom: 24px;
    margin-bottom: -24px;

    overflow-x: scroll;
    overflow-y: hidden;
    scroll-behavior: smooth;
    -webkit-overflow-scrolling: touch;
    scroll-snap-type: x mandatory;
  }

  .ContentCard {
    scroll-snap-align: start;
    width: calc(100vw - 24px);

    padding-left: 24px;
    margin-right: -8px;
  }

  .ExpandHeight .ContentCard {
    width: calc(75vw - 24px);
  }

  .ArrowButton {
    visibility: hidden;
  }

  @media (min-width: 480px) {
    .ContentCard {
      width: calc((100vw - 16px) / 2);
    }

    .ExpandHeight .ContentCard {
      width: calc((100vw - 16px) / 2.5);
    }
  }

  @media (min-width: 920px) {
    .ContentList {
      overflow-x: hidden;
    }

    .ContentCard {
      width: calc((100vw - 8px) / 3);
    }

    .ExpandHeight .ContentCard {
      width: calc((100vw - 8px) / 3.5);
    }

    .ArrowButton {
      visibility: initial;
    }
  }

  @media (min-width: 1200px) {
    .ContentListParent {
      margin-left: -12px;
      margin-right: -12px;
      width: calc(100% + 24px);
    }

    .ContentList {
      display: flex;
    }

    .ContentCard {
      padding: 0 12px;

      flex: 0 0 33.33333%;
      max-width: 33.33333%;
      margin: 0;
    }

    .ExpandHeight .ContentCard {
      flex: 0 0 25%;
      max-width: 25%;
    }
  }

  @media (min-width: 1440px) {
    .ContentCard {
      flex: 0 0 25%;
      max-width: 25%;
    }

    .ExpandHeight .ContentCard {
      flex: 0 0 16.66666%;
      max-width: 16.66666%;
    }
  }
</style>

<style scoped lang="less">
  .Control {
    width: 46px;
    height: 46px;

    border-radius: 23px;
    background: white;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);

    &::after {
      position: absolute;
      content: "";

      top: calc(50%);
      margin-top: -6px;
      height: 14px;
      width: 14px;
      border-right: 2px solid rgba(0, 0, 0, 0.85);
      border-bottom: 2px solid rgba(0, 0, 0, 0.85);
    }
  }

  .Control.Prev {
    margin-left: -18px;
    margin-right: auto;

    &::after {
      margin-left: 19px;
      transform: rotate(135deg);
    }
  }

  .Control.Next {
    margin-left: auto;
    margin-right: -18px;

    &::after {
      margin-left: 13px;
      transform: rotate(-45deg);
    }
  }
</style>
