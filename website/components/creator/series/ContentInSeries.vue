<template>
  <div class="p-12" @click="more = true">
    <div class="bg-whisper100 border-3 p-16-24 hover-pointer">
      <div v-if="content">
        <h3 class="text-ellipsis-2l">{{content.title}}</h3>
        <p class="text-ellipsis-3l">{{content.body}}</p>
        <div class="small">{{sortId}}</div>
      </div>
      <div v-else>
        <h3>Loading</h3>
      </div>
    </div>

    <div v-if="more">
      <portal to="dialog-w768">
        <div>
          <input-text label="SortId" v-model="changeSort"/>
          <p class="mtb-16">SortId determine the arrangement of Content in the series. Descending order.</p>
          <div class="flex-end">
            <button class="secondary" @click="$emit('change-sort', changeSort)">Apply Sort</button>
          </div>
        </div>


        <hr class="mtb-16">
        <div>
          <h3 class="mb-16">Remove from series</h3>
          <input-text label="Type: (remove from series)" v-model="deleteConfirm"/>
          <div class="flex-end" v-if="deleteConfirm === 'remove from series'">
            <button class="danger mt-16" @click="$emit('delete')">Confirm</button>
          </div>
        </div>

        <div class="flex-end mtb-16">
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
      return {content: null, more: false, changeSort: this.sortId, deleteConfirm: ''}
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
