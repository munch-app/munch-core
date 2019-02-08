<template>
  <div class="bg-whisper100 border-3 p-8-16 flex-wrap">
    <div class="mr-8 flex-center">
      <h5 class="m-0">ADD:</h5>
    </div>

    <div class="AddItem" v-for="add in addList" :key="add.type">
      <button v-if="!add.available || add.available.includes(story.type)" class="small border" @click="onAdd(add.type)">
        {{add.name}}
      </button>
    </div>

    <div class="flex-align-center flex-justify-end ml-8 flex-grow">
      <button class="More secondary-outline small mr-16" @click="onMore">
        {{ saving ? 'Saving': ''}}
        <simple-svg v-if="!saving" class="wh-24px" fill="#084E69" :filepath="require('~/assets/icon/more.svg')"/>
      </button>
      <button class="secondary small" @click="onPublish">Publish</button>
    </div>

    <portal to="dialog-action-sheet" v-if="more">
      <div @click="onSave">
        {{ saving ? 'Saving': 'Save'}} Story
      </div>
      <div @click="onLink">
        Open Story Link
      </div>
      <div @click="onDelete">
        Delete Story
      </div>
      <hr>
      <div @click="more = false" class="close">
        Close
      </div>
    </portal>

    <portal to="dialog-styled" v-if="deleting">
      <h3>Are you sure?</h3>
      <p>Once the story is deleted, you cannot recover it.</p>
      <div class="right">
        <button class="border" @click="deleting = false">Cancel</button>
        <button class="secondary" @click="onConfirmDelete">Confirm</button>
      </div>
    </portal>
  </div>
</template>

<script>
  export default {
    name: "CreatorAddItem",
    data() {
      return {
        addList: [
          {name: 'Place', type: 'munchPlace'},
          {name: 'Text', type: 'body'},
          {name: 'Line', type: 'line'},
          {name: 'Image', type: 'image'},
          {name: 'HTML', type: 'html', available: ['blog']},
        ],
        more: false,
        deleting: false,
        saving: false,
      }
    },
    props: {
      story: {
        type: Object,
        required: true,
      },
    },
    methods: {
      onAdd(type) {
        this.$emit('add', type)
      },
      onPublish() {
        this.$emit('publish')
      },
      onLink() {

      },
      onSave() {
        this.$emit('save')
        this.saving = true

        setTimeout(() => {
          this.saving = false
        }, 3000)
      },
      onDelete() {
        this.more = false
        this.deleting = true
      },
      onConfirmDelete() {
        this.deleting = false

        return this.$api.delete(`/creators/${this.story.creatorId}/stories/${this.story.storyId}`).then(() => {
          this.$router.push({path: '/creator/stories'})
        }).catch((err) => this.$store.dispatch('addError', err))
      },
      onMore() {
        this.more = true
      }
    }
  }
</script>

<style scoped lang="less">
  .AddItem {
    margin: 8px;
  }

  button.More {
    height: 32px;
    padding-top: 0 !important;
    padding-bottom: 0 !important;
  }
</style>
