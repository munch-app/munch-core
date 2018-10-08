<template>
  <div>
    <div class="SearchCard primary-500-bg border-4 zero-spacing">
      <h2 class="white text weight-600">Can't decide?</h2>
      <div class="white text">{{description}}</div>

      <horizontal-scroll-view class="TagList" :items="card.tags" :map-key="i => i.name" :container="false"
                              :nav-white="false" :padding="18">
        <template slot-scope="{item}">
          <div class="Tag text text-center white-bg border-4 hover-pointer" @click="onClick(item.name)">
            <div class="Name text-ellipsis-1-line">{{item.name}}</div>
            <div class="Count text-ellipsis-1-line">{{item.count}} places</div>
          </div>
        </template>
      </horizontal-scroll-view>
    </div>
  </div>
</template>

<script>
  import HorizontalScrollView from "../../core/HorizontalScrollView";

  export default {
    name: "SearchCardSuggestionTag",
    components: {HorizontalScrollView},
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    computed: {
      description() {
        if (this.card.locationName) {
          return `Here are some suggestions of what’s good in ${this.card.locationName}`
        }
        return 'Here are some suggestions of what’s good nearby.'
      }
    },
    methods: {
      onClick(tag) {
        this.$store.dispatch('filter/tag', tag)
        this.$store.dispatch('search/start', this.$store.state.filter.query)
      }
    }
  }
</script>

<style scoped lang="less">
  .SearchCard {
    padding: 24px 24px;
    overflow: hidden;
  }

  .TagList {
    margin-top: 16px;
    height: 70px !important;

    .Tag {
      padding-left: 6px;
      padding-right: 6px;

      width: 120px;
      height: 70px;
      font-weight: 600;

      .Name {
        text-transform: capitalize;
        padding-top: 13px;
        font-size: 14px;
        color: rgba(0, 0, 0, 0.85);
      }

      .Count {
        font-size: 13px;
        color: rgba(0, 0, 0, 0.77);
      }
    }
  }
</style>
