<template>
  <div class="ContentBanner bg-white elevation-light zero">
    <div class="container-1200 flex-justify-between">
      <div>
        <div class="flex-align-center">
          <h5>{{content.profile && content.profile.name || 'Content'}}</h5>
          <h5 class="mlr-8">|</h5>
          <h5 class="s500 text-ellipsis-1l">{{content.subtitle}}</h5>
        </div>
        <h1 class="weight-700 mtb-16">{{content.title}}</h1>
        <p class="large">{{content.body}}</p>
      </div>

      <div class="RightPanel flex-end">
        <social-share :url="url"/>
      </div>
    </div>
  </div>
</template>

<script>
  import SocialShare from "../core/SocialShare";
  export default {
    name: "ContentBanner",
    components: {SocialShare},
    props: {
      content: {
        type: Object,
        required: true
      },
      url: {
        type: String,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">
  .ContentBanner {
    padding: 64px 0;
  }

  .RightPanel {
    @media (min-width: 992px) {
      flex: 0 0 33%;
      height: 33%;
      margin-left: 128px;
    }
  }

  .large {
    line-height: 1.7;
  }
</style>
