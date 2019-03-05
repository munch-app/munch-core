<template>
  <div class="bg-white elevation-light zero">
    <div class="ContentBanner container-1200 flex-justify-between">
      <div>
        <h5 class="text-ellipsis-1l">
          <span>{{content.profile && content.profile.name || 'Content'}}</span>
          <span class="mlr-8">|</span>
          <span class="s500">{{content.subtitle}}</span>
        </h5>
        <h1 class="weight-700 mtb-16">{{content.title}}</h1>
        <p class="large">{{content.body}}</p>
      </div>

      <div class="RightPanel flex-justify-end">
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
    padding-top: 64px;
    padding-bottom: 64px;


    flex-wrap: wrap;

    @media (min-width: 992px) {
      flex-wrap: nowrap;
    }
  }

  .LeftPanel {

  }

  .RightPanel {
    margin-top: 24px;

    @media (min-width: 992px) {
      flex: 0 0 33%;
      margin-left: 128px;
    }
  }

  .large {
    line-height: 1.7;
  }
</style>
