/**
 * Policy Mappings
 * (sails.config.policies)
 *
 * Policies are simple functions which run **before** your controllers.
 * You can apply one or more policies to a given controller, or protect
 * its actions individually.
 *
 * Any policy file (e.g. `api/policies/authenticated.js`) can be accessed
 * below by its filename, minus the extension, (e.g. "authenticated")
 *
 * For more information on how policies work, see:
 * http://sailsjs.org/#!/documentation/concepts/Policies
 *
 * For more information on configuring policies, check out:
 * http://sailsjs.org/#!/documentation/reference/sails.config/sails.config.policies.html
 */


module.exports.policies = {

  /***************************************************************************
  *                                                                          *
  * Default policy for all controllers and actions (`true` allows public     *
  * access)                                                                  *
  *                                                                          *
  ***************************************************************************/

  // '*': true,
  /*UserController: {
    changePass: 'sessionAuth'
  }*/
  ThreadController: {
    createThread: 'tokenAuth',
    deleteThread: 'tokenAuth',
    reportThread: 'tokenAuth',
    voteThread: 'tokenAuth'
  },
  CommentController: {
    reportComment: 'tokenAuth',
    createComment: 'tokenAuth',
    voteComment: 'tokenAuth'
  },
  UserController: {
    deactivateAccount: 'tokenAuth',
    updateProfile: 'tokenAuth',
    viewProfile: 'tokenAuth',
    blockUser: 'tokenAuth',
    unblockUser: 'tokenAuth',
    changePassword: 'tokenAuth'
  },
  ConversationController: {
    newConversation: 'tokenAuth',
    getConversation: 'tokenAuth'
  },

  MessageController: {
    newMessage: 'tokenAuth',
    getMessage: 'tokenAuth'
  },

  InformationController: {
    createInfo: 'sessionAuth',
    deleteInfo: 'sessionAuth',
  },

  "website/WebpageControllerController": {
    renderMainPage: 'sessionAuth',
    renderAllInfoPage: 'sessionAuth',
    deleteInfos: "sessionAuth",
    renderAddInfoPage: 'sessionAuth',
    addInfo: 'sessionAuth',
    editInfo: 'sessionAuth',
    renderAllFaqsPage: 'sessionAuth',
    deleteFaqs: "sessionAuth",
    renderAddFaqPage: 'sessionAuth',
    addFaq: 'sessionAuth',
    editFaq: 'sessionAuth'
  }

  /***************************************************************************
  *                                                                          *
  * Here's an example of mapping some policies to run before a controller    *
  * and its actions                                                          *
  *                                                                          *
  ***************************************************************************/
	// RabbitController: {

		// Apply the `false` policy as the default for all of RabbitController's actions
		// (`false` prevents all access, which ensures that nothing bad happens to our rabbits)
		// '*': false,

		// For the action `nurture`, apply the 'isRabbitMother' policy
		// (this overrides `false` above)
		// nurture	: 'isRabbitMother',

		// Apply the `isNiceToAnimals` AND `hasRabbitFood` policies
		// before letting any users feed our rabbits
		// feed : ['isNiceToAnimals', 'hasRabbitFood']
	// }
};
