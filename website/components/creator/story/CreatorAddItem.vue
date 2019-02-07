<template>
  <div class="bg-whisper100 border-3 p-8-16 flex-wrap">
    <div class="mr-16 flex-center">
      <h5 class="m-0">ADD:</h5>
    </div>

    <div class="AddItem" v-for="add in addList" :key="add.type">
      <button class="small border" @click="onAdd(add.type)">{{add.name}}</button>
    </div>

    <div class="flex-align-center flex-justify-end ml-8 flex-grow">
      <button class="secondary-outline small mr-16" @click="onSave">
        {{saving ? 'Saving' : 'Save'}}
      </button>
      <button class="secondary small" @click="onPublish">Publish</button>
    </div>
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
          {name: 'HTML', type: 'html'},
        ]
      }
    },
    props: {
      saving: {
        type: Boolean,
        required: true,
      }
    },
    methods: {
      onAdd(type) {
        this.$emit('add', type)
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
  .AddItem {
    margin: 8px;
  }
</style>
