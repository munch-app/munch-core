<template>
  <div class="Assumption">
    <simple-svg class="Icon" fill="rgba(0,0,0,0.75)" filepath="/img/search/assumption.svg"/>
    <div v-for="token in tokens" :key="token.type + token.text">
      <div class="Tag Token Border24 Whisper100Bg BlackA80" v-if="token.type === 'tag'">{{token.text}}</div>
      <div class="Text Token Border24 BlackA75" v-if="token.type === 'text'">
        <div>
          {{token.text}}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "SearchSuggestAssumptionItem",
    props: {
      assumption: {
        type: Object,
        required: true
      }
    },
    computed: {
      tokens() {
        return this.assumption.tokens
      }
    }
  }
</script>

<style scoped lang="less">
  .Assumption {
    height: 28px;
    display: flex;
    justify-content: flex-start;
    align-items: center;

    .Icon {
      float: left;
      width: 24px;
      height: 24px;
      margin: 1px 15px 1px 0;
    }

    .Token {
      font-size: 15px;
      font-weight: 600;

      line-height: 28px;
      height: 28px;
      margin-right: 8px;

      &.Tag {
        padding: 0 12px;
      }

      &.Text {
        padding: 0;
        font-weight: 600;
      }
    }
  }
</style>
