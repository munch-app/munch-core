<template>
  <div class="container pt-48 pb-128">
    <div v-for="(node, index) in nodes" :key="index">
      <home-featured v-if="index === 0" :node="node"/>
      <home-publication v-else :node="node"/>
    </div>
  </div>
</template>
<script>
  import HomeFeatured from "../components/home/HomeFeatured";
  import HomePublication from "../components/home/HomePublication";

  export default {
    components: {HomePublication, HomeFeatured},
    head() {
      return this.$head({
        robots: {follow: true, index: true},
        url: `https://www.munch.app/`,
      })
    },
    asyncData({$api}) {
      return $api.get('/pages/home')
        .then(({data: nodes}) => {
          return {nodes}
        })
    },
  }
</script>
