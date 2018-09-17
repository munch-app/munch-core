<template>
  <div class="Container ZeroSpacing LandingPage">
    <section class="Profile">
      <div class="Name">
        <h1>Good morning, Samantha</h1>
        <div class="Text Login">(not your name? <span class="LoginButton Primary500 Weight600">Log In</span> now!)</div>
      </div>
      <p>Discover the best wherever & whenever</p>
    </section>

    <section class="Timing">
      <h2>Here & Now</h2>
      <p>The best Breakfast spots near you.</p>
    </section>

    <section class="Location">
      <h2>Day in, day out</h2>
      <p>Your favourite places - rediscovered.</p>
      <landing-location-list/>
    </section>

    <section class="General">
      <h2>In the know</h2>
      <p>What’s hot and edible</p>
    </section>
  </div>
</template>

<script>
  import LandingLocationList from "../components/landing/LandingLocationList";

  export default {
    components: {LandingLocationList},
    layout: 'search'
  }
</script>

<style scoped lang="less">
  .LandingPage {
    margin-top: 24px;
    margin-bottom: 24px;
  }

  section {
    margin-top: 24px;
  }

  section.Profile {
    .Name {
      display: flex;
      align-items: flex-end;

      h1 {

      }

      .Login {
        margin-left: 24px;
        line-height: 30px;

        .LoginButton {
          &:hover {
            cursor: pointer;
            text-decoration: underline;
          }
        }
      }
    }
  }

  section.Location {

  }
</style>
