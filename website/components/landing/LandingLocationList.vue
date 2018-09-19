<template>
  <div class="LandingLocationList container">
    <div class="LocationList no-select">
      <div class="elevation-1 elevation-hover-2 hover-pointer border-2 EachLocation" v-for="location in locations"
           :key="location.name">
        <div class="ImageBox Primary050Bg">
          <simple-svg fill="rgba(0, 0, 0, 0.75)" :filepath="`/img/landing/${location.image}`"/>
        </div>
        <div class="ContentBox">
          <div class="Name">
            {{location.name}}
          </div>
          <div class="Address text">
            {{location.address}}
          </div>
        </div>
      </div>
      <div class="AddLocation EachLocation">
        <div class="elevation-1 elevation-hover-2 hover-pointer border-2">
          <div class="ImageBox">
            <simple-svg fill="rgba(0, 0, 0, 0.75)" :filepath="`/img/landing/add.svg`"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "LandingLocationList",
    data() {
      return {
        locations: [
          {image: 'home.svg', name: 'Home', address: 'Bishan Street 55', description: ''},
          {image: 'work.svg', name: 'Work', address: '73 Ayer Rajah Crescent', description: ''},
        ]
      }
    }
  }
</script>

<style scoped lang="less">
  .LocationList {
    margin-left: -24px;
    margin-right: -24px;
    padding-top: 16px;
    padding-bottom: 8px;

    display: flex;
    overflow-y: visible;
    overflow-x: scroll;
    -webkit-overflow-scrolling: touch;

    .EachLocation:nth-of-type(1) {
      margin-left: 24px;
    }

    .EachLocation:nth-last-of-type(1) {
      padding-right: 24px;
    }

    .EachLocation {
      flex-shrink: 0;
      display: flex;
      margin-right: 24px;
      width: 248px;

      div.ImageBox {
        width: 64px;
        height: 64px;
        padding: 16px;
      }

      div.ContentBox {
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;
        padding-top: 8px;
        padding-left: 16px;
        padding-right: 16px;

        .Name {
          font-weight: 600;
          font-size: 17px;
          line-height: 1.3;
          color: rgba(0, 0, 0, 0.75);
        }

        .Address {
          font-size: 15px;

          text-overflow: ellipsis;
          white-space: nowrap;
          overflow: hidden;
        }
      }
    }

    .AddLocation {
      width: initial;

      .Image {
        padding: 18px;
      }
    }
  }
</style>
