package com.chalnakchalnak.account.member.application.port.in;

import com.chalnakchalnak.account.member.application.command.RegisterMemberCommand;
import com.chalnakchalnak.account.member.application.command.UpdateMemberCommand;
import com.chalnakchalnak.account.member.application.query.MemberListQuery;
import com.chalnakchalnak.account.member.application.result.ChatMemberResult;
import com.chalnakchalnak.account.member.application.result.MemberResult;
import java.util.List;

public interface MemberUseCase {

  ChatMemberResult getChatMember(String memberUuid);

  MemberResult getMember(String memberUuid);

  List<MemberResult> getMemberList(MemberListQuery memberUuidListDto);

  List<MemberResult> getAllMemberList();

  void updateDynamic(UpdateMemberCommand memberUpdateRequestDto);

  void signUp(RegisterMemberCommand signUpRequestDto);

  Boolean existNickname(String nickname);
}
