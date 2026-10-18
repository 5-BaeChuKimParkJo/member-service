package com.chalnakchalnak.account.member.adapter.out.persistence;

import com.chalnakchalnak.account.member.application.command.UpdateMemberCommand;

public interface MemberUpdateRepository {

  long updateDynamic(UpdateMemberCommand memberUpdateRequestDto);
}
