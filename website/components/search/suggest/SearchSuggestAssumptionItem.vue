<template>
  <div class="flex-align-center">
    <simple-svg class="wh-24px mr-16" fill="rgba(0,0,0,0.75)"
                :filepath="require('~/assets/icon/search/assumption.svg')"/>
    <div v-for="token in tokens" :key="token.type + token.text">
      <div :class="{'bg-whisper100': !highlight, 'bg-white': highlight}" class="Tag Token border-3 b-a80"
           v-if="token.type === 'tag'">
        {{token.text}}
      </div>
      <div class="text Token border-3 b-a75" v-if="token.type === 'text'">
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
      },
      highlight: {
        type: Boolean,
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
  .Token {
    font-size: 15px;
    font-weight: 600;

    line-height: 28px;
    height: 28px;
    margin-right: 8px;
  }

  .Tag {
    padding: 0 12px;
  }
</style>
