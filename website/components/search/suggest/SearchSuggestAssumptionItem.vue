<template>
  <div class="Assumption">
    <div v-for="token in tokens" :key="token.type + token.text">
      <div class="Token Border24 Whisper100Bg Tag" v-if="token.type === 'tag'">{{token.text}}</div>
      <div class="Token Border24 Text" v-if="token.type === 'text'">{{token.text}}</div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "SearchSuggestAssumptionItem",
    props: {
      item: {
        type: Object,
        required: true
      }
    },
    computed: {
      tokens() {
        return this.item.assumption.tokens
      }
    }
  }
</script>

<style scoped lang="less">
  .Assumption {
    display: flex;
    justify-content: flex-start;
    align-items: center;

    .Token {
      font-size: 14px;
      font-weight: 400;

      line-height: 26px;
      height: 26px;
      margin-right: 8px;

      &.Tag {
        padding: 0 11px;
      }

      &.Text {
        padding: 0 4px;
      }
    }
  }
</style>
