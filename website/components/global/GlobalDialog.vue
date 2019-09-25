<!--
  GlobalDialog is created to facilitate the use of dialog through the entire website.
  Global dialog will only allow one dialog to appear at a time.
  All dialog must be registered in this component.
-->
<template>
  <div class="bg-fog fixed position-0 index-dialog flex-center" v-if="anyDialog" @click.self.stop.capture="onClose">
    <div>
      <div class="flex-justify-end" v-if="isCloseable">
        <div class="index-elevation absolute p-8 hover-pointer" @click="onClose">
          <div class="elevation-1 border-circle bg-white p-8">
            <simple-svg class="wh-24px" fill="black" :filepath="require('~/assets/icon/close.svg')"/>
          </div>
        </div>
      </div>

      <div class="GlobalDialog">
        <loading-dialog v-if="dialogName === 'LoadingDialog'"/>
        <portal-target v-else-if="dialogName === 'PortalDialog'" name="PortalDialog"/>
        <place-editor-dialog v-else-if="dialogName === 'PlaceEditorDialog'" v-bind="dialogProps"/>
        <get-started-dialog v-else-if="dialogName === 'GetStartedDialog'" v-bind="dialogProps"/>
        <image-upload-dialog v-else-if="dialogName === 'ImageUploadDialog'" v-bind="dialogProps"/>

        <search-tag-dialog v-else-if="dialogName === 'SearchTagDialog'" v-bind="dialogProps"/>
        <editor-lat-lng-dialog v-else-if="dialogName === 'EditorLatLngDialog'" v-bind="dialogProps"/>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  import LoadingDialog from "../dialog/LoadingDialog";
  import GetStartedDialog from "../dialog/GetStartedDialog";
  import ImageUploadDialog from "../dialog/ImageUploadDialog";
  import PortalDialog from "../dialog/PortalDialog";
  import PlaceEditorDialog from "../dialog/PlaceEditorDialog";
  import SearchTagDialog from "../dialog/SearchTagDialog";
  import EditorLatLngDialog from "../dialog/EditorLatLngDialog";

  export default {
    name: "GlobalDialog",
    components: {
      EditorLatLngDialog,
      SearchTagDialog, PlaceEditorDialog, PortalDialog, ImageUploadDialog, GetStartedDialog, LoadingDialog
    },
    computed: {
      ...mapGetters('global', ['dialogName', 'dialogProps', 'anyDialog']),
      isCloseable() {
        return this.dialogName !== 'LoadingDialog'
      }
    },
    watch:{
      $route (to, from){
        this.$store.commit('global/clearDialog')
      }
    },
    methods: {
      onClose() {
        if (this.isCloseable) {
          this.$store.commit('global/clearDialog')
        }
      },
    }
  }
</script>
