<template>
  <div class="dialog-large">
    <editor-lat-lng v-model="editing" pre-state="searching"/>
    <div class="flex-end mt-24">
      <button class="blue" @click="done">Done</button>
    </div>
  </div>
</template>

<script>

  import EditorLatLng from "../editor/EditorLatLng";

  export default {
    name: "EditorLatLngDialog",
    components: {EditorLatLng},
    props: {
      latLng: {
        type: String,
      },
      onLatLng: {
        type: Function,
      },
    },
    data() {
      return {
        editing: this.latLng
      }
    },
    watch: {
      editing() {
        this.onLatLng(this.editing)
      }
    },
    methods: {
      done() {
        this.$store.commit('global/clearDialog')
      }
    }
  }
</script>
