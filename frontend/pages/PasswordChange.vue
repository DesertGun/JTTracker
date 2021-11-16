<template>
  <div class="main">
    <b-container fluid>
      <b-row>
        <b-col />
        <b-col>
          <h3>Update your password</h3>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container fluid>
      <b-row>
        <b-col />
        <b-col>
          Please enter your current-password for varification!
          <b-form-group id="passwordFormGroup" label-for="password">
            <div v-if="responseMassage">
              <b-form-input
                id="password"
                v-model="passwordCurrent"
                disabled
                type="password"
              />
            </div>
            <div v-else>
              <b-form-input
                id="password"
                v-model="passwordCurrent"
                type="password"
              />
            </div>
          </b-form-group>
          Please enter your new password:
          <b-form-group id="passwordNewFormGroup1" label-for="change1">
            <div v-if="responseMassage">
              <b-form-input
                id="change1"
                v-model="passwordNew"
                disabled
                type="password"
              />
            </div>
            <div v-else>
              <b-form-input
                id="change1"
                v-model="passwordNew"
                type="password"
              />
            </div>
          </b-form-group>
          Please enter it again:
          <b-form-group id="passwordNewFormGroup2" label-for="change2">
            <div v-if="responseMassage">
              <b-form-input
                id="change2"
                v-model="passwordCheck"
                disabled
                type="password"
              />
            </div>
            <div v-else>
              <b-form-input
                id="change2"
                v-model="passwordCheck"
                type="password"
              />
            </div>
            <b-form-invalid-feedback :state="validationPassword">
              Both password don't match or are too short (minimum length is 8)!
            </b-form-invalid-feedback>
          </b-form-group>
          <b-button variant="secondary" @click="changeReq">
            Update my password !
          </b-button>
          <div v-if="responseSuccess" class="pt-2" style="text-align: center">
            <b-alert dismissible show variant="success">
              {{ responseSuccess }}
            </b-alert>
          </div>
          <div v-if="responseError" class="pt-2" style="text-align: center">
            <b-alert dismissible show variant="danger">
              {{ responseError }}
            </b-alert>
          </div>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
  </div>
</template>

<script>
export default {
  middleware: 'authenticated',
  asyncData() {
    return {
      passwordCurrent: '',
      passwordNew: '',
      passwordCheck: '',
      responseSuccess: '',
      responseError: '',
    }
  },
  computed: {
    validationPassword() {
      return (
        this.passwordCheck === this.passwordNew && this.passwordNew.length >= 8
      )
    },
  },
  methods: {
    async changeReq() {
      try {
        const response = await this.$axios.post('/user/password/update', {
          newPassword: this.passwordNew,
          oldPassword: this.passwordCurrent,
        })
        if (response.data.validated === true) {
          this.responseSuccess = response.data.successMessage
        } else {
          this.responseError = response.data.errorMessage
        }
      } catch (e) {
        alert(e.toString())
      }
    },
  },
}
</script>
