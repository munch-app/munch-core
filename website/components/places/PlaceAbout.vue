<template>
  <div v-if="hasAny">
    <h2>About</h2>
    <place-award-list class="AwardList" v-if="awards && awards.length > 0" :awards="awards"/>
    <p class="Description" v-if="place.description">{{place.description}}</p>
  </div>
</template>

<script>
  import PlaceAwardList from "./PlaceAwardList";

  export default {
    name: "PlaceAbout",
    components: {PlaceAwardList},
    props: {
      awards: {
        type: Array,
        required: true
      },
      place: {
        type: Object,
        required: true
      }
    },
    computed: {
      hasAny() {
        return (this.awards && this.awards.length > 0)
          || this.place.description
      }
    }
  }
</script>

<style scoped lang="less">
  .AwardList {
    margin-top: 16px;
    margin-bottom: 16px;
  }

  .Description {
    margin-top: 16px;
  }
</style>
