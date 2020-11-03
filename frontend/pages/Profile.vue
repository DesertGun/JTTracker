<template>
  <div class="profileMain">
    <b-container class="profileHeaderContainer" fluid>
      <b-row>
        <b-col />
        <b-col>
          <h3>
            Profile
          </h3>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container class="profileContentContainer" fluid>
      <b-row>
        <b-col />
        <b-col>
          <b-form>
            <b-form-group
              id="usernameFormGroup"
              description="Keep in mind, that your Username is your contact adress!"
              label="Username:"
              label-for="username"
            >
              <b-form-input id="username" v-model="username" disabled="true" type="text" />
            </b-form-group>
            <b-form-group
              id="accountNameFormGroup"
              description="With the following name you will be visible for other users. The choice is up to you. Default is your username."
              label="Account name:"
              label-for="accountName"
            >
              <b-form-input id="accountName" v-model="accountName" placeholder="None" type="text" />
              <b-form-invalid-feedback :state="validationaccountNameChange">
                Your global name cannot be empty!
              </b-form-invalid-feedback>
              <b-form-invalid-feedback :state="validationaccountNameNew">
                No changes detected !
              </b-form-invalid-feedback>
              <b-form-valid-feedback :state="validationaccountNameChange" />
            </b-form-group>
            <b-form-group
              id="avatarFormGroup"
              description="Your personal Accout-Avatar"
              label="Avatar:"
              label-for="avatar"
            >
              <img :src="'https://gravatar.com/avatar/${hash}?d=identicon'">
            </b-form-group>
            <b-form-group
              id="passwordFormGroup"
              description="You will receive a password-reset link to the adress (username) provided above"
              label="Password:"
              label-for="password"
            >
              <b-form-input id="password" v-model="dummyPassword" disabled type="password" />
            </b-form-group>
            <b-button variant="secondary" @click="changePassword">
              Change current password
            </b-button>
            <div
              v-if="validationaccountNameChange && this.accountName !== this.username && validationaccountNameNew"
              class="pt-2"
            >
              <b-button variant="success" @click="updateProfile">
                Update my profile
              </b-button>
            </div>
            <div v-if="updated" class="pt-2" style="text-align: center">
              <b-alert dismissible show variant="success">
                Your profile was updated successfully!
              </b-alert>
            </div>
          </b-form>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
  </div>
</template>

<script>
export default {
  middleware: 'authenticated',
  computed: {
    validationaccountNameChange () {
      return this.accountName.length > 0
    },
    validationaccountNameNew () {
      return this.accountName !== this.initaccountName
    }
  },
  asyncData () {
    return {
      username: null,
      accountName: '',
      initaccountName: '',
      dummyPassword: 'placeHolderPasswordForRenderingPurposes',
      changed: false,
      updated: false,
      hash: null
    }
  },
  async mounted () {
    try {
      const response = await this.$axios.get('/user')
      this.username = response.data.username
      if (response.data.accountName) {
        this.accountName = response.data.accountName
        this.initaccountName = this.accountName
        this.hash = response.data.hash
      } else {
        this.accountName = this.username
        this.initaccountName = this.accountName
      }
    } catch (e) {
      alert(e.toString())
    }
  },
  methods: {
    async updateProfile () {
      try {
        const response = await this.$axios.put('/user/update', {
          username: this.username,
          accountName: this.accountName
        })
        if (response.data.validated === true) {
          this.updated = true
        }
      } catch (e) {
        alert(e.toString())
      }
    },
    changePassword () {
      this.$router.push('/passwordChange')
    }
  }
}
</script>
