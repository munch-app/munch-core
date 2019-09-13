<script>
  export default {
    name: "PlaceStatus",
    props: {
      status: {
        type: Object,
        required: true
      }
    }
  }
</script>

<template>
  <div v-if="status.type === 'DORMANT'" class="p-16-24 border-3 hover-pointer bg-steam">
    <!-- TODO(fuxing): Suggest Edit -->
    <h4>This place has been inactive for awhile.</h4>
    <div class="flex mt-4">
      <div class="regular mr-8">Is this place information still accurate?</div>
      <div class="bg-pink p-4-8 border-2 overflow-hidden flex-align-center">
        <simple-svg class="wh-16px" :filepath="require('~/assets/icon/icons8-trust.svg')" fill="#FFF"/>
        <div class="white tiny-bold ml-4">HELP US</div>
      </div>
    </div>
  </div>

  <div v-else-if="status.type === 'HIDDEN'" class="p-16-24 border-3 hover-pointer bg-steam">
    <h4>This place is recently added.</h4>
    <div class="flex mt-4">
      <div class="regular mr-8">Is the information accurate?</div>
      <div class="bg-pink p-4-8 border-2 overflow-hidden flex-align-center">
        <simple-svg class="wh-16px" :filepath="require('~/assets/icon/icons8-trust.svg')" fill="#FFF"/>
        <div class="white tiny-bold ml-4">HELP US</div>
      </div>
    </div>
  </div>

  <div v-else-if="status.type === 'DELETED'" class="p-16-24 border-3 hover-pointer bg-error">
    <h4 class="white">This place has been deleted.</h4>
    <div class="flex mt-4" v-if="status.reason">
      <div class="regular white">{{status.reason}}</div>
    </div>
  </div>
  <div v-else-if="status.type === 'MOVED'" class="p-16-24 border-3 hover-pointer bg-error">
    <h4 class="white">This place has been moved.</h4>
    <div class="flex mt-4" v-if="status.id">
      <nuxt-link :to="`/${status.id}`">https://munch.app/{{status.id}}</nuxt-link>
    </div>
  </div>
  <div v-else-if="status.type === 'PERMANENTLY_CLOSED'" class="p-16-24 border-3 hover-pointer bg-error">
    <h4 class="white">Permanently Closed</h4>
    <div class="flex mt-4">
      <div class="regular white mr-8">Is this information accurate?</div>
    </div>
  </div>
  <div v-else/>
</template>
