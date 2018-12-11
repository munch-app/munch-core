<template>
  <div>
    <section class="container-remove-gutter">
      <div class="container overlay wh-100 flex-align-center inline-flex">
        <div class="flex-column">
          <h1 class="white">Discover by neighbourhood</h1>
          <p class="white">Enter a location and we’ll tell you what’s delicious around.</p>
          <button class="s700 mt-24" @click="isDialog = true">Enter Location</button>
        </div>
      </div>
    </section>

    <search-filter-area-dialog v-if="isDialog" @cancel="isDialog = false"/>
  </div>
</template>

<script>
  import SearchFilterAreaDialog from "../../dialog/SearchFilterAreaDialog";
  export default {
    name: "SearchCardLocationBanner",
    components: {SearchFilterAreaDialog},
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        isDialog: false
      }
    }
  }
</script>

<style scoped lang="less">
  //noinspection CssUnknownTarget
  section {
    margin-top: -30px;

    background: url("~/assets/img/search/location-banner.jpg") no-repeat center/cover;
    height: 300px;
  }

  .overlay {
    background: rgba(0, 0, 0, 0.4);
  }

  button {
    align-self: flex-end;
  }
</style>
