<template>
  <div>
    <div class="SearchCard Primary500Bg Border4 ZeroSpacing">
      <h2 class="White Text Weight600">Can't decide?</h2>
      <div class="White Text">{{description}}</div>

      <div class="TagList">
        <div class="TagContainer" v-for="tag in card.tags" :key="tag.name" @click="onClick(tag.name)">
          <div class="Tag Text TextCenter WhiteBg Border4">
            <div class="Name">{{tag.name}}</div>
            <div class="Count">{{tag.count}} places</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "SearchCardSuggestionTag",
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
        this.$store.dispatch('filter/start')
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
    display: flex;

    overflow-x: scroll;
    -webkit-overflow-scrolling: touch;

    .TagContainer {
      padding-right: 18px;
    }

    .Tag {
      padding-left: 6px;
      padding-right: 6px;

      width: 120px;
      height: 70px;
      font-weight: 600;

      &:hover {
        cursor: pointer;
      }

      div {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }

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
