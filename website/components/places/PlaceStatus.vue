<template>
  <div v-if="status" class="zero">
    <div class="p-16-24 bg-error border-3 mb-24 white">
      <h3 class="white">{{status.title}}</h3>
      <p v-if="status.message">{{status.message}} <a class="white" :href="suggestEditUrl" target="_blank">Suggest an
        edit.</a></p>
    </div>
  </div>
</template>

<script>
  const types = {
    'open': null,
    'renovation': {
      title: 'Under Renovation',
      message: 'Know this place?',
    },
    'closed': {
      title: 'Permanently Closed',
      message: 'Know this place?',
    },
    'moved': {
      title: 'Permanently Moved',
      message: 'Know this place?',
    },
    'deleted': {
      title: 'Deleted from Munch',
      message: 'This place has permanently closed or removed from Munch. Know this place?',
    },
    // TODO renamed and redirect need to be build
    'renamed': {
      title: 'This place is renamed.',
    },
    'redirected': {
      title: 'This place has been redirected.'
    }
  }

  export default {
    name: "PlaceStatus",
    props: {
      place: {
        type: Object,
        required: true
      }
    },
    computed: {
      status() {
        return types[this.place.status.type]
      },
      suggestEditUrl() {
        return `/places/suggest?placeId=${this.place.placeId}`;
      }
    }
  }
</script>

<style scoped lang="less">

</style>
