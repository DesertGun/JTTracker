<template>
  <div class="profileMain">
    <b-container class="profileHeaderContainer" fluid>
      <b-row>
        <b-col />
        <b-col>
          <div style="text-align: center">
            <h3>Profile</h3>
          </div>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container class="profileContentContainer" fluid>
      <b-row>
        <b-col />
        <b-col style="min-width: fit-content">
          <b-card>
            <b-card-body>
              <b-form>
                <b-row>
                  <b-col>
                    <b-form-group
                      id="usernameFormGroup"
                      description="Unique username for yor account"
                      label="Username"
                      label-for="username"
                    >
                      <b-form-input
                        id="username"
                        v-model="username"
                        disabled="true"
                        type="text"
                      />
                    </b-form-group>
                  </b-col>
                  <b-col>
                    <b-form-group
                      id="accountNameFormGroup"
                      description="Name linked to your account"
                      label="Account name"
                      label-for="accountName"
                    >
                      <b-form-input
                        id="accountName"
                        v-model="accountName"
                        placeholder="None"
                        type="text"
                      />
                    </b-form-group>
                  </b-col>
                </b-row>
                <b-row>
                  <b-col />
                  <b-col>
                    <b-form-invalid-feedback
                        :state="validationaccountNameChange"
                      >
                        Your global name cannot be empty!
                      </b-form-invalid-feedback>
                      <b-form-invalid-feedback
                        :state="validationaccountNameNew"
                      >
                        No changes detected !
                      </b-form-invalid-feedback>
                      <b-form-valid-feedback
                        :state="validationaccountNameChange"
                      />
                  </b-col>
                  <b-col />
                </b-row>
                <b-row>
                  <b-col />
                  <b-col>
                    <div
                      v-if="
                        validationaccountNameChange &&
                        accountName !== username &&
                        validationaccountNameNew
                      "
                      class="pb-2"
                    >
                      <b-button
                        class="w-100"
                        variant="success"
                        @click="updateProfile"
                      >
                        Update profile
                      </b-button>
                    </div>
                  </b-col>
                  <b-col />
                </b-row>
                <b-row>
                  <b-col />
                  <b-col style="min-width: fit-content;max-width: min-content;">
                    <div v-if="updated" class="mt-2" style="text-align: center">
                      <b-alert dismissible show variant="success">
                        Your profile was updated successfully!
                      </b-alert>
                    </div>
                  </b-col>
                  <b-col />
                </b-row>
                <b-row>
                  <b-col />
                  <b-col class="avatar-content">
                    <b-form-group
                      id="avatarFormGroup"
                      label="Avatar"
                      label-for="avatar"
                    >
                      <div v-if="!hasProfilePicture">
                        <b-img-lazy
                          :src="
                            'https://gravatar.com/avatar/' +
                            getHash +
                            '?d=identicon'
                          "
                          height="100px"
                          width="100px"
                          rounded="circle"
                        />
                      </div>
                      <div v-else>
                        <b-img-lazy
                          :src="getProfilePicture"
                          height="100px"
                          width="100px"
                          rounded="circle"
                        />
                      </div>
                    </b-form-group>
                  </b-col>
                  <b-col />
                </b-row>
                <b-row>
                  <b-col />
                  <b-col style="min-width: fit-content">
                    <b-form-file
                      v-model="profilePictureUpload"
                      class="mb-2"
                      accept=".jpg"
                    ></b-form-file>
                    <div v-if="uploadCurrent > 0">
                      <b-progress
                        :value="uploadCurrent"
                        :max="uploadMax"
                        show-progress
                        animated
                      ></b-progress>
                    </div>
                    <b-row>
                      <b-col />
                      <b-col style="min-width: fit-content">
                        <div v-if="profilePictureUpload">
                          <b-button
                            variant="primary"
                            class="mt-2"
                            @click="uploadProfilePicture"
                          >
                            Upload my profile picture !
                          </b-button>
                        </div>
                        <div v-if="hasProfilePicture">
                          <b-button
                            variant="outline-danger"
                            class="mt-2"
                            @click="deleteProfilePicture"
                          >
                            Delete my profile picture !
                          </b-button>
                        </div>
                      </b-col>
                      <b-col />
                    </b-row>
                    <b-row>
                      <b-col />
                      <b-col style="min-width: fit-content;max-width: min-content;">
                        <div
                          v-if="responseSuccess"
                          class="pt-2"
                          style="text-align: center"
                        >
                          <b-alert dismissible show variant="success">
                            {{ responseSuccess }}
                          </b-alert>
                        </div>
                        <div
                          v-if="responseError" class="pt-2"
                          style="text-align: center">
                            <b-alert dismissible show variant="danger">
                            {{ responseError }}
                          </b-alert>
                        </div>
                      </b-col>
                      <b-col />
                    </b-row>
                  </b-col>
                  <b-col />
                </b-row>

                <b-row>
                  <b-col />
                  <b-col style="min-width: fit-content">
                    <b-button
                      class="w-100 mt-2"
                      variant="outline-secondary"
                      @click="changePassword"
                    >
                      Change password
                    </b-button>
                  </b-col>
                  <b-col />
                </b-row>

                <b-row>
                  <b-col />
                  <b-col>
                    <div v-if="hasSecurityEnabled">
                      <b-button
                        variant="outline-danger"
                        class="mt-2 w-100"
                        @click="removeEnchancedSecurity"
                      >
                        Disable 2FA !
                      </b-button>
                    </div>
                    <div v-else>
                      <b-button
                        variant="success"
                        class="mt-2 w-100"
                        @click="openQuestions"
                      >
                        Enable 2FA !
                      </b-button>
                    </div>
                  </b-col>
                  <b-col />
                </b-row>
                <b-row>
                  <b-col />
                  <b-col style="min-width: max-content">
                    <div v-if="removeSec">
                      Please enter the answer for the security question !
                      <br />
                      {{ securityQuestion1 }}
                      <b-form-input
                        v-model="securityAnswer1"
                        class="mt-2"
                      ></b-form-input>
                      <br />
                      <div v-if="securityAnswer1">
                        {{ securityQuestion2 }}
                        <b-form-input
                          v-model="securityAnswer2"
                          class="mt-2"
                        ></b-form-input>
                      </div>
                      <br />
                      <div v-if="securityAnswer2">
                        {{ securityQuestion3 }}
                        <b-form-input
                          v-model="securityAnswer3"
                          class="mt-2"
                        ></b-form-input>
                      </div>
                    </div>
                    <div
                      v-if="
                        securityAnswer1 &&
                        securityAnswer2 &&
                        securityAnswer3 &&
                        removeSec
                      "
                      style="text-align: center"
                    >
                      <b-button
                        variant="danger"
                        class="mt-2"
                        @click="disableSecurity()"
                      >
                        Yes, disable 2FA !
                      </b-button>
                    </div>
                    <div v-if="enableSecurity">
                      <b-form-group id="input-group-2fa" label-for="choice-2fa">
                        <b-form-select
                          v-model="firstQuestion"
                          :options="questions"
                          class="mt-2"
                        ></b-form-select>
                        <div v-if="firstQuestion">
                          <b-form-input
                            v-model="firstAnswer"
                            class="mt-2"
                          ></b-form-input>
                          <br />
                        </div>
                        <div v-if="firstAnswer">
                          <b-form-select
                            v-model="secondQuestion"
                            :options="questions"
                            class="mt-2"
                          ></b-form-select>
                        </div>
                        <div v-if="secondQuestion">
                          <b-form-input
                            v-model="secondAnswer"
                            class="mt-2"
                          ></b-form-input>
                          <br />
                        </div>
                        <div v-if="secondAnswer">
                          <b-form-select
                            v-model="thirdQuestion"
                            :options="questions"
                            class="mt-2"
                          ></b-form-select>
                        </div>
                        <div v-if="thirdQuestion">
                          <b-form-input
                            v-model="thirdAnswer"
                            class="mt-2"
                          ></b-form-input>
                        </div>
                        <b-form-invalid-feedback
                          :state="validationSecurityQuestions"
                        >
                          You need to select 3 unique questions and answer them
                          !
                        </b-form-invalid-feedback>
                        <b-form-valid-feedback
                          :state="validationSecurityQuestions"
                        >
                          Security questions are OK !
                        </b-form-valid-feedback>
                        <div v-if="thirdAnswer">
                          <div
                            v-if="
                              enableSecurity &&
                              firstAnswer &&
                              secondAnswer &&
                              thirdAnswer &&
                              validationSecurityQuestions
                            "
                          >
                            2FA can be activated !
                            <b-row>
                              <b-col style="text-align: left">
                                <b-button
                                  variant="success"
                                  class="mt-2"
                                  @click="submitSecurityQuestions"
                                >
                                  Submit
                                </b-button>
                              </b-col>
                              <b-col />
                              <b-col style="text-align: right">
                                <b-button
                                  variant="outline-danger"
                                  class="mt-2"
                                  @click="closeSecurityQuestions"
                                >
                                  Forget it
                                </b-button>
                              </b-col>
                            </b-row>
                          </div>
                        </div>
                      </b-form-group>
                    </div>
                  </b-col>
                  <b-col />
                </b-row>
                <b-row class="mt-2" align-h="between">
                              <b-col cols="4"/>
                              <b-col cols="4" style="min-width: fit-content;text-align: end">
                                <b-button variant="outline-danger" @click="deleteAccount()">
                                  <b-icon icon="trash-fill" />
                                </b-button>
                              </b-col>
                            </b-row>
              </b-form>
            </b-card-body>
          </b-card>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'ProfilePage',
  middleware: 'authenticated',
  asyncData() {
    return {
      username: null,
      accountName: '',
      initaccountName: '',
      changed: false,
      updated: false,
      profilePictureUpload: null,
      responseError: null,
      responseSuccess: null,
      uploadCurrent: 0,
      uploadMax: 100,
      enableSecurity: null,
      firstQuestion: null,
      secondQuestion: null,
      thirdQuestion: null,
      firstAnswer: null,
      secondAnswer: null,
      thirdAnswer: null,
      securityQuestion1: null,
      securityQuestion2: null,
      securityQuestion3: null,
      securityAnswer1: null,
      securityAnswer2: null,
      securityAnswer3: null,
      removeSec: null,
      questions: [
        { value: null, text: 'Select a question !' },
        {
          value: 'What is the name of your first pet?',
          text: 'What is the name of your first pet?',
        },
        {
          value: 'What is the name of your first school?',
          text: 'What is the name of your first school?',
        },
        { value: 'What was your first job?', text: 'What was your first job?' },
        {
          value: 'What is your favorite color?',
          text: 'What is your favorite color?',
        },
        {
          value: 'What is your favorite date in history?',
          text: 'What is your favorite date in history?',
        },
      ],
    }
  },
  computed: {
    validationSecurityQuestions() {
      return (
        this.firstQuestion !== this.secondQuestion &&
        this.firstQuestion !== this.thirdQuestion &&
        this.secondQuestion !== this.thirdQuestion &&
        this.firstAnswer &&
        this.secondAnswer &&
        this.thirdAnswer
      )
    },
    validationaccountNameChange() {
      return this.accountName.length > 0
    },
    validationaccountNameNew() {
      return this.accountName !== this.initaccountName
    },
    ...mapGetters({
      getProfilePicture: 'user/getProfilePicture',
      getHash: 'user/getHash',
      hasProfilePicture: 'user/hasProfilePicture',
      isLoggedIn: 'auth/isLoggedIn',
      getUsername: 'user/getUsername',
      getAccountname: 'user/getAccountname',
      hasSecurityEnabled: 'user/hasSecurityEnabled',
    }),
  },
  mounted() {
    try {
      this.username = this.getUsername
      if (this.getAccountname) {
        this.accountName = this.getAccountname
        this.initaccountName = this.accountName
      } else {
        this.accountName = this.username
        this.initaccountName = this.accountName
      }
      this.hash = this.getHash
      this.debugSec = this.hasSecurityEnabled
    } catch (e) {
      alert(e.toString())
    }
  },
  methods: {
    deleteAccount(){
      this.$router.push('/accountDelete')
    },
    async removeEnchancedSecurity() {
      this.removeSec = true
      try {
        const securityResponse = await this.$axios.get(
          '/security/' + this.username
        )
        this.securityQuestion1 = securityResponse.data.securityQuestion1
        this.securityQuestion2 = securityResponse.data.securityQuestion2
        this.securityQuestion3 = securityResponse.data.securityQuestion3
      } catch (e) {
        alert(e.toString())
      }
    },
    async disableSecurity() {
      try {
        const response = await this.$axios.post('/security/validate', {
          username: this.username,
          securityAnswer1: this.securityAnswer1,
          securityAnswer2: this.securityAnswer2,
          securityAnswer3: this.securityAnswer3,
        })

        if (response.data.validated === true) {
          const disableSecurityResponse = await this.$axios.post(
            '/security/disable',
            {
              username: this.username,
            }
          )
          if (disableSecurityResponse.data.successMessage) {
            this.responseSuccess = disableSecurityResponse.data.successMessage
            this.firstQuestion = null
            this.secondQuestion = null
            this.thirdQuestion = null
            this.firstAnswer = null
            this.secondAnswer = null
            this.thirdAnswer = null
            await this.$store.dispatch('user/setProfileData')
            this.removeSec = false
          }
        } else if (response.data.errorMessage) {
          this.responseError = response.data.errorMessage
        }
      } catch (e) {
        alert(e.toString())
      }
    },
    openQuestions() {
      this.enableSecurity = true
    },
    closeSecurityQuestions() {
      this.firstQuestion = null
      this.secondQuestion = null
      this.thirdQuestion = null
      this.firstAnswer = null
      this.secondAnswer = null
      this.thirdAnswer = null
      this.enableSecurity = false
    },
    async submitSecurityQuestions() {
      try {
        const response = await this.$axios.post('/security/enable', {
          username: this.username,
          securityQuestion1: this.firstQuestion,
          securityQuestion2: this.secondQuestion,
          securityQuestion3: this.thirdQuestion,
          securityAnswer1: this.firstAnswer,
          securityAnswer2: this.secondAnswer,
          securityAnswer3: this.thirdAnswer,
        })
        if (response.data.successMessage) {
          this.closeSecurityQuestions()
          this.securityAnswer1 = null
          this.securityAnswer2 = null
          this.securityAnswer3 = null
          this.removeSec = false
          this.responseSuccess = response.data.successMessage
          await this.$store.dispatch('user/setProfileData')
        } else {
          this.responseError = response.data.errorMessage
        }
      } catch (e) {
        alert(e.toString())
      }
    },
    async updateProfile() {
      try {
        const response = await this.$axios.put('/user/update', {
          username: this.username,
          accountName: this.accountName,
        })
        if (response.data.validated === true) {
          this.updated = true
          this.accountName = this.getAccountname
          this.initaccountName = this.accountName
        }
        await this.$store.dispatch('user/setProfileData')
      } catch (e) {
        alert(e.toString())
      }
    },
    async uploadProfilePicture() {
      try {
        const formData = new FormData()
        formData.append('profilePicture', this.profilePictureUpload)

        const response = await this.$axios.post('/user/picture', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
          onUploadProgress: function (progressEvent) {
            this.uploadCurrent = parseInt(
              Math.round((progressEvent.loaded / progressEvent.total) * 100)
            )
          }.bind(this),
        })
        this.profilePictureUpload = null
        if (response.data.validated === true) {
          this.responseSuccess = response.data.successMessage
          await this.$store.dispatch('user/setProfilePicture')
          this.uploadCurrent = 0
          this.profilePicture = this.getProfilePicture
        } else {
          this.responseError = response.data.errorMessage
        }
      } catch (e) {
        alert(e.toString())
      }
    },
    async deleteProfilePicture() {
      try {
        const response = await this.$axios.delete('/user/picture')
        if (response.data.successMessage) {
          this.responseSuccess = response.data.successMessage
          this.$store.dispatch('user/deleteProfilePicture')
        }
      } catch (e) {
        alert(e.toString())
      }
    },
    changePassword() {
      this.$router.push('/passwordChange')
    },
  },
}
</script>

<style>
.avatar-content {
  text-align: center;
}
</style>
