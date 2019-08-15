<template>
  <portal to="creator-header-right">
    <div class="flex-align-center mr-8 relative">
      <button @click="onPublish" class="blue-outline tiny mr-8" v-if="!publish">Ready to publish?</button>
      <button @click="more = !more" class="small">
        <simple-svg class="wh-20px" fill="black" :filepath="require('~/assets/icon/more.svg')"/>
      </button>

      <div class="CreatorHeader absolute no-select" v-if="more"
           @click="more = false" v-on-clickaway="() => {if(more) more = false}">
        <div class="border-3 bg-white w-100 elevation-2 text index-top-elevation border lh-1">
          <div v-if="!publish" @click="onSave">Save content</div>
          <div>
            <a :href="`/contents/${cid}`" target="_blank">
              Preview content
            </a>
          </div>
          <div @click="deleting = true">Delete content</div>
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
  import base64 from 'uuid-base64'

  export default {
    name: "ContentNavHeader",
    data() {
      return {
        more: false,
        deleting: false,
      }
    },
    props: {
      publish: {
        type: Boolean,
        default: false
      },
      contentId: {
        type: String
      }
    },
    computed: {
      cid() {
        return base64.encode(this.contentId)
      }
    },
    methods: {
      onDelete() {
        this.$emit('delete')
      },
      onSave() {
        this.$emit('save')
      },
      onPublish() {
        this.$emit('publish')
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
