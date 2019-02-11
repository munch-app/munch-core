<template>
  <div class="p-12" @click="more = true">
    <div class="bg-whisper100 border-3 p-16-24">
      <div v-if="content">
        <h3 class="text-ellipsis-2l">{{content.title}}</h3>
        <p>{{content.body}}</p>
        <div class="small">{{sortId}}</div>
      </div>
      <div v-else>
        <h3>Loading</h3>
      </div>
    </div>

    <div v-if="more">
      <portal to="dialog-styled">
        <input-text label="SortId" v-model="changeSort"/>
        <button class="secondary" @click="$emit('change-sort', changeSort)">Apply Sort Id Changes</button>

        <div class="right">
          <button class="border" @click="more = false">Cancel</button>
        </div>
      </portal>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import InputText from "../../core/InputText";

  export default {
    name: "ContentInSeries",
    components: {InputText},
    props: {
      contentId: {
        type: String,
        required: true
      },
      sortId: {
        type: String
      }
    },
    data() {
      return {content: null, more: false, changeSort: ''}
    },
    computed: {
      ...mapGetters('creator', ['creatorId']),
    },
    mounted() {
      this.$api.get(`/creators/${this.creatorId}/contents/${this.contentId}`)
        .then(({data}) => {
          this.content = data
        })
    }
  }
</script>

<style scoped lang="less">

</style>
