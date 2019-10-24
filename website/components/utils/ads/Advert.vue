<template>
  <div v-if="visible">
    <adsbygoogle v-if="google"
                 :ad-slot="toString(google.slot)"
                 :ad-layout="google.layout"
                 :ad-format="google.format"
                 :ad-style="google.style"
    />
  </div>
</template>

<script>

  /**
   * You hide all ads by adding querystring vs=YWRzLWhpZGU
   */
  export default {
    name: "Advert",
    props: {
      google: {
        type: Object
      }
    },
    computed: {
      visible() {
        return this.$vs.none('ads-hide')
      }
    },
  }
</script>
