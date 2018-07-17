<template>
  <div class="NavHeader NavBg Elevation1">
    <b-container class="Row">
      <a href="/"><img class="MunchLogo" src="/img/MunchLogo32.svg"></a>

      <input class="SearchBar Elevation1 Border24" type="text" placeholder="Search e.g. Italian in Marina Bay">
    </b-container>
  </div>
</template>

<style lang="less" scoped>
  .NavHeader {
    height: 64px;

    .Row {
      display: flex;
      flex-direction: row;
    }
  }

  .MunchLogo {
    margin-top: 16px;
    margin-bottom: 16px;
    height: 32px;
    width: 32px;
  }

  .SearchBar {
    margin-top: 16px;
    margin-bottom: 16px;
    font-size: 13px;
    background-color: #FFFFFF;
    height: 32px;
    margin-left: 24px;
    color: rgba(0, 0, 0, 0.6);

    border: none transparent;
    padding: 7px 12px;

    &:focus {
      outline: none;
    }
  }

  @media only screen and (max-width: 499.8px) {
    .SearchBar {
      flex-grow: 2;
      margin-right: 24px;
    }
  }

  @media only screen and (min-width: 500px) {
    .SearchBar {
      width: 360px;
    }
  }
</style>
