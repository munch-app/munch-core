<template>
  <div>
    <place-award-list class="AwardList" v-if="awards && awards.length > 0" :awards="awards"/>
    <div v-if="place.description" @click="expanded = !expanded" class="hover-pointer">
      <p class="Description" :class="{'text-ellipsis-4l': !expanded}">{{place.description}}</p>
    </div>
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
    data() {
      return {
        expanded: false
      }
    },
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
