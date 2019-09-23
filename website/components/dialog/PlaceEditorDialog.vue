<template>
  <div class="dialog-large">
    <place-editor ref="editor" :place="place"/>
    <div class="flex-end mt-24">
      <button class="blue" @click="submit">Submit</button>
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
      },
      onSubmit: {
        type: Function,
      },
    },
    methods: {
      submit() {
        return this.$refs.editor.confirm(({place}) => {
          this.onSubmit(place)
        })
      }
    }
  }
</script>
