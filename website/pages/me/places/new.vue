<template>
  <div class="container-768 pt-48 pb-128">
    <div class="mb-48">
      <h1>Create a new place</h1>
    </div>

    <place-editor ref="editor" :place="place"/>
    <div class="flex-end mt-24">
      <button class="blue" @click="onSubmit">Submit</button>
    </div>
  </div>
</template>

<script>
  import PlaceEditor from "../../../components/places/PlaceEditor";

  export default {
    components: {PlaceEditor},
    head() {
      return {title: `New Place · Munch`}
    },
    data() {
      return {
        place: {}
      }
    },
    methods: {
      onSubmit() {
        return this.$refs.editor.confirm(({place}) => {
          this.$api.post(`/places`, place)
            .then(() => {
              this.$store.dispatch('addMessage', {title: 'Added New Revision', message: 'Thanks for contributing!'})
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

</style>
