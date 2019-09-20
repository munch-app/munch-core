<!--
  GlobalDialog is created to facilitate the use of dialog through the entire website.
  Global dialog will only allow one dialog to appear at a time.
  All dialog must be registered in this component.
-->
<template>
  <div class="bg-fog fixed position-0 index-dialog flex-center" v-if="anyDialog" @click.self.stop.capture="dismiss">
    <div>
      <div class="flex-justify-end" v-if="dialog !== 'LoadingDialog'">
        <div class="absolute p-8 hover-pointer" @click="dismiss">
          <div class="border-circle bg-white p-8">
            <simple-svg class="wh-24px" fill="black" :filepath="require('~/assets/icon/close.svg')"/>
          </div>
        </div>
      </div>

      <loading-dialog class="global-dialog" v-if="dialog === 'LoadingDialog'"/>
      <get-started-dialog class="global-dialog" v-if="dialog === 'GetStartedDialog'"/>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  import LoadingDialog from "../dialog/LoadingDialog";
  import GetStartedDialog from "../dialog/GetStartedDialog";

  export default {
    components: {GetStartedDialog, LoadingDialog},
    computed: {
      ...mapGetters('global', ['dialog', 'anyDialog']),
    },
    methods: {
      dismiss() {
        // Loading Dialog cannot be dismissed
        if (this.dialog === 'LoadingDialog') {
          return
        }

        this.$store.commit('global/clearDialog')
      },
    }
  }
</script>
