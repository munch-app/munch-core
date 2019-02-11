<template>
  <portal to="creator-header-right">
    <div class="flex-align-center mr-8 relative">
      <button @click="more = !more" class="small">
        <simple-svg class="wh-20px" fill="black" :filepath="require('~/assets/icon/more.svg')"/>
      </button>

      <div class="CreatorHeader absolute no-select" v-if="more"
           @click="more = false" v-on-clickaway="() => {if(more) more = false}">
        <div class="border-3 bg-white w-100 elevation-2 text index-top-elevation border lh-1">
          <div @click="onSave">Save series</div>
          <div @click="deleting = true">Delete series</div>
          <hr>
          <div @click="onStatus('published')">Publish series</div>
          <div @click="onStatus('archived')">Archive series</div>
        </div>
      </div>
    </div>

    <div>
      <portal to="dialog-styled" v-if="deleting">
        <h3>Are you sure?</h3>
        <p>Once the content is deleted, you cannot recover it.</p>

        <div class="right">
          <button class="border" @click="deleting = false">Cancel</button>
          <button class="secondary" @click="onDelete">Confirm</button>
        </div>
      </portal>
    </div>
  </portal>
</template>

<script>
  export default {
    name: "SeriesNavHeader",
    data() {
      return {
        more: false,
        deleting: false,
      }
    },
    methods: {
      onDelete() {
        this.$emit('delete')
      },
      onSave() {
        this.$emit('save')
      },
      onStatus(status) {
        this.$emit('change-status', status)
      }
    }
  }
</script>

<style scoped lang="less">
  .CreatorHeader {
    min-width: 160px;

    top: 40px;
    right: 0;

    > div {
      padding: 8px 0;

      > div {
        padding: 8px 24px;
        font-size: 14px;

        &:hover {
          cursor: pointer;
        }
      }
    }
  }
</style>
