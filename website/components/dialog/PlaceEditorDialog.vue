<!-- Non global dialog -->

<template>
  <div class="fixed position-0 bg-overlay flex-center index-dialog">
    <div>
      <div class="flex-justify-end">
        <div class="CloseButton absolute hover-pointer" @click="onClose">
          <simple-svg class="wh-24px" fill="black" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
        </div>
      </div>

      <div class="Dialog dialog-large" v-on-clickaway="onClose">
        <div class="bg-white">
          <place-editor ref="editor" :place="place"/>
          <div class="flex-end mt-24">
            <button class="blue" @click="onSubmit">Submit</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import PlaceEditor from "../places/PlaceEditor";

  export default {
    name: "PlaceEditorDialog",
    components: {PlaceEditor},
    props: {
      place: {
        type: Object,
      }
    },
    methods: {
      onClose() {
        this.$emit('on-close')
      },
      onSubmit() {
        return this.$refs.editor.confirm(({place}) => {
          this.$api.post(`/places/${place.id}/revisions`, place)
            .then(() => {
              this.$store.dispatch('addMessage', {title: 'Added Revision', message: 'Thanks for contributing!'})
              this.$emit('on-close')
            })
            .catch(err => {
              this.$store.dispatch('addError', err)
            })
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .CloseButton {
    margin-top: -32px;
  }
</style>
